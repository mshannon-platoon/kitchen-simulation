package ie.plat.demo.order;

import static java.lang.Thread.sleep;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.plat.demo.config.ConfigProperties;
import ie.plat.demo.kitchen.KitchenService;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderProcessService {

  private final ConfigProperties configProperties;
  private final KitchenService kitchenService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public OrderProcessService(ConfigProperties configProperties, KitchenService kitchenService) {
    this.configProperties = configProperties;
    this.kitchenService = kitchenService;
  }

  public void processOrders() throws IOException {
    log.info("OrderProcessor - processOrders()");
    List<Order> orders = importOrders();
    orders.forEach(order -> {
      kitchenService.acceptOrder(order);

      sleep();
    });
  }

  private List<Order> importOrders() throws IOException {
    File file = new File(
        Objects.requireNonNull(this
                .getClass()
                .getClassLoader()
                .getResource(configProperties.getOrdersFilename()))
            .getFile());

    Order[] orders = objectMapper.readValue(file, Order[].class);
    log.info("OrderProcessor - importOrders() - totalOrders: {}", orders.length);
    return Arrays.asList(orders);
  }

  private void sleep(){
    try {
      Thread.sleep(1000 / configProperties.getOrdersPerSecond());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
