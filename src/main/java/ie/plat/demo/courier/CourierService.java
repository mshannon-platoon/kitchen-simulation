package ie.plat.demo.courier;

import ie.plat.demo.AllocationService;
import ie.plat.demo.config.ConfigProperties;
import ie.plat.demo.shelf.ShelfService;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CourierService {

  private final AllocationService allocationService;
  private final ShelfService shelfService;

  private static final Integer MIN_SEC = 2;
  private static final Integer MAX_SEC = 6;
  private final ScheduledExecutorService scheduledExecutorService;
  private final Map<String, ScheduledFuture<?>> orderIdToScheduledFutureMap = new HashMap<>();

  private final Random random = new Random();

  public CourierService(AllocationService allocationService, ShelfService shelfService,
      ConfigProperties configProperties) {
    this.allocationService = allocationService;
    this.shelfService = shelfService;
    this.scheduledExecutorService = Executors.newScheduledThreadPool(
        configProperties.getScheduledThreadPoolSize());
  }

  public void scheduleCourier(String orderId) {
    Integer delay = randomBetween();

    orderIdToScheduledFutureMap.put(orderId,
        scheduledExecutorService.schedule(new Courier(orderId, allocationService, shelfService), delay, TimeUnit.SECONDS));

    log.info("Scheduled a courier for orderId: {} delay generated: {}", orderId, delay);
  }

  public void cancelCourier(String orderId) {
    ScheduledFuture<?> scheduledFuture = orderIdToScheduledFutureMap.get(orderId);
    if (scheduledFuture.isDone()) {
      log.info("CourierService - cancelCourier - already complete! orderId: {}", orderId);
      return;
    }

    scheduledFuture.cancel(true);
    log.info("Cancelled a courier for orderId: {}", orderId);
  }

  public Integer randomBetween() {
    return random.nextInt(MAX_SEC - MIN_SEC + 1) + MIN_SEC;
  }

}
