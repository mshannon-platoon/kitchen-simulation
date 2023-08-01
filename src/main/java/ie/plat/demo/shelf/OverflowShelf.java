package ie.plat.demo.shelf;

import ie.plat.demo.order.CookedOrder;
import ie.plat.demo.utils.OrderUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OverflowShelf extends AbstractShelf {

  protected OverflowShelf(Integer maxOrdersOnShelf, Integer shelfModifier) {
    super(maxOrdersOnShelf, shelfModifier);
  }

  @Override
  public synchronized boolean putOrderIfThereIsSpace(CookedOrder order){
    Map<String, CookedOrder> overflowShelf = getShelf();
    if (overflowShelf.size() <= getMaxOrdersOnShelf()) {
      overflowShelf.put(order.getOrder().id(), order);
    } else {
      removeOrderClosestToExpiry();

      overflowShelf.put(order.getOrder().id(), order);
    }

     // orderAllocationService.updateAllocation(CookedOrder);


    // orderAllocationService.markOrderAsWaste(CookedOrder)


    return true;
  }

  @Override
  public synchronized void cleanShelf(String orderId) {

    // this shelf has a different behavior

    // we need to loop through the whole shelf, and re-allocate based on capacity of each shelf
  }

  private void removeOrderClosestToExpiry() {
    List<CookedOrder> orderList = new ArrayList<>(getShelf().values());

    String orderId = OrderUtils.findOldestOrderOnShelf(orderList, getShelfModifier());

    getShelf().remove(orderId);


    // orderAllocationService.markOrderAsWaste(CookedOrder)
    // orderAllocationService.updateAllocation(CookedOrder);
  }

}
