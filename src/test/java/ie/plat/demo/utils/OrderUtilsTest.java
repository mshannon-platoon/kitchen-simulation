package ie.plat.demo.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ie.plat.demo.order.CookedOrder;
import ie.plat.demo.order.Order;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OrderUtilsTest {

  @Test
  void testFindOldestOrderOnShelf() {
    assertEquals("200", OrderUtils.findOldestOrderOnShelf(cookedOrderList(), 1));
  }

  private List<CookedOrder> cookedOrderList() {
    Order order1 = new Order("100", "y", "z", 20, 0.64f, new Date());
    Order order2 = new Order("200", "y", "z", 10, 0.70f, new Date());
    Order order3 = new Order("300", "y", "z", 15, 0.75f, new Date());

    return List.of(
        new CookedOrder(order1, LocalDateTime.parse("2023-08-01T20:50:30")),
        new CookedOrder(order2, LocalDateTime.parse("2023-08-01T20:50:55")),
        new CookedOrder(order3, LocalDateTime.parse("2023-08-01T20:50:45")));
  }

}
