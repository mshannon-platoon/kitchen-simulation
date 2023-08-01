package ie.plat.demo.courier;

import ie.plat.demo.AllocationService;
import ie.plat.demo.order.CookedOrder;
import ie.plat.demo.shelf.ShelfService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Courier implements Runnable {

  private String orderId;

  private AllocationService allocationService;

  private ShelfService shelfService;

  @Override
  public void run() {
    // move the allocation code into the shelf service ( the courier doesn't need to know )
    String allocation = allocationService.getAllocation(orderId);
    Optional<CookedOrder> cookedOrderOptional = shelfService.collectOrder(orderId, allocation);

    if(cookedOrderOptional.isPresent()){
      log.info("orderId: {} was sucessfully collected", orderId);
    }else {
      log.info("orderId: {} FAILED to be collected", orderId);
    }
  }
}
