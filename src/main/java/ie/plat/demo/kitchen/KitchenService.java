package ie.plat.demo.kitchen;

import ie.plat.demo.courier.CourierService;
import ie.plat.demo.order.CookedOrder;
import ie.plat.demo.order.Order;
import ie.plat.demo.shelf.ShelfService;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KitchenService {

  private final CourierService courierService;
  private final ShelfService shelfService;

  public KitchenService(CourierService courierService, ShelfService shelfService) {
    this.courierService = courierService;
    this.shelfService = shelfService;
  }

  public void acceptOrder(Order order) {
    //log.info("acceptOrder() - orderId: {}", order.id());
    dispatchCourierForOrder(order.id());
    cookOrder(order);
  }

  private void cookOrder(Order order) {
    //log.info("cookOrder() - orderId: {}", order.id());
    shelfOrder(new CookedOrder(order, LocalDateTime.now()));
  }

  private void shelfOrder(CookedOrder cookedOrder) {
    //log.info("shelfOrder() - orderId: {}", cookedOrder.getOrder().id());
    shelfService.allocateShelf(cookedOrder);
  }

  private void dispatchCourierForOrder(String orderId) {
    log.info("dispatchCourierForOrder() - orderId: {}", orderId);
    courierService.scheduleCourier(orderId);
  }

}
