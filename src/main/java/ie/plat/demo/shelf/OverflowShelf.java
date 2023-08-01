package ie.plat.demo.shelf;

import ie.plat.demo.AllocationService;
import ie.plat.demo.courier.CourierService;
import ie.plat.demo.order.CookedOrder;
import ie.plat.demo.utils.OrderUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OverflowShelf extends AbstractShelf {

  private final  HotShelf hotShelf;
  private final ColdShelf coldShelf;
  private final FrozenShelf frozenShelf;
  private final AllocationService allocationService;


  protected OverflowShelf(Integer maxOrdersOnShelf, Integer shelfModifier, HotShelf hotShelf, ColdShelf coldShelf, FrozenShelf frozenShelf, AllocationService allocationService) {
    super(maxOrdersOnShelf, shelfModifier);
    this.hotShelf = hotShelf;
    this.coldShelf = coldShelf;
    this.frozenShelf = frozenShelf;
    this.allocationService = allocationService;
  }

  @Override
  public synchronized boolean putOrderIfThereIsSpace(CookedOrder order){
    Map<String, CookedOrder> overflowShelf = getShelf();
    if (overflowShelf.size() <= getMaxOrdersOnShelf()) {
      overflowShelf.put(order.getOrder().id(), order);
    } else {
      removeOrderClosestToExpiry();

     // overflowShelf.put(order.getOrder().id(), order);
    }

    return true;
  }

  public void reorderShelves(HotShelf hotShelf, ColdShelf coldShelf, FrozenShelf frozenShelf) {
    this.cleanShelf();

    List<String> hotOrders = new ArrayList<>();
    List<String> coldOrders = new ArrayList<>();
    List<String> frozenOrders = new ArrayList<>();

    Map<String, CookedOrder> overflowShelf = getShelf();
    overflowShelf.forEach((key, value) -> {
      switch (value.getOrder().temp()) {
        case "hot" -> hotOrders.add(key);
        case "cold" -> coldOrders.add(key);
        case "frozen" -> frozenOrders.add(key);
      }
    });

    hotOrders.forEach(id -> {
      CookedOrder hotOrder = overflowShelf.get(id);
      if(hotShelf.putOrderIfThereIsSpace(hotOrder)){
        allocationService.putAllocation(id, "hot");
        overflowShelf.remove(id);
      }
    });

    coldOrders.forEach(id -> {
      CookedOrder coldOrder = overflowShelf.get(id);
      if(coldShelf.putOrderIfThereIsSpace(coldOrder)){
        allocationService.putAllocation(id, "cold");
        overflowShelf.remove(id);
      }
    });

    frozenOrders.forEach(id -> {
      CookedOrder frozenOrder = overflowShelf.get(id);
      if(frozenShelf.putOrderIfThereIsSpace(frozenOrder)){
        allocationService.putAllocation(id, "frozen");
        overflowShelf.remove(id);
      }
    });

  }

  private void removeOrderClosestToExpiry() {
    List<CookedOrder> orderList = new ArrayList<>(getShelf().values());

    String orderId = OrderUtils.findOldestOrderOnShelf(orderList, getShelfModifier());

    CookedOrder cookedOrder = getShelf().get(orderId);
    boolean success = switch (cookedOrder.getOrder().temp()) {
      case "hot" -> hotShelf.putOrderIfThereIsSpace(cookedOrder);
      case "cold" -> coldShelf.putOrderIfThereIsSpace(cookedOrder);
      case "frozen" -> frozenShelf.putOrderIfThereIsSpace(cookedOrder);
      default -> false;
    };

    if(success){
      getShelf().remove(orderId);
      allocationService.putAllocation(orderId, cookedOrder.getOrder().temp());

      log.info("orderId: {} was reallocated to the {} shelf", orderId, cookedOrder.getOrder().temp());

    } else {

      // we have to remove a random one
    }
  }

}
