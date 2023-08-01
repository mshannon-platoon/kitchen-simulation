package ie.plat.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AllocationService {

  private final Map<String, String> allocationMap = new HashMap<>();

  private final AtomicInteger wasteCounter = new AtomicInteger();

  /**
   * I think this whole idea of an allocation map is flawed..
   *
   * Can there be a race condition where there's loads of waste?
   *
   * @param orderId
   * @param shelfAllocation
   */
  public void putAllocation(String orderId, String shelfAllocation) {
    log.info("orderId: {} was allocated to the {} Shelf", orderId, shelfAllocation);
    allocationMap.put(orderId, shelfAllocation);
  }

  public String getAllocation(String orderId) {
    return allocationMap.get(orderId);
  }

  public void incrementWasteCounter(){
    Integer waste = wasteCounter.incrementAndGet();
    log.info("allocationService total waste so far: {}", waste);
  }

}
