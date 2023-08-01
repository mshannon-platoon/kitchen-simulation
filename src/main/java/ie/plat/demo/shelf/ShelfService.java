package ie.plat.demo.shelf;

import ie.plat.demo.AllocationService;
import ie.plat.demo.order.CookedOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShelfService {

  private final AllocationService allocationService;

  private final HotShelf hotShelf = new HotShelf(10, 1);

  private final ColdShelf coldShelf = new ColdShelf(10, 1);

  private final FrozenShelf frozenShelf = new FrozenShelf(10, 1);
  private final OverflowShelf overflowShelf = new OverflowShelf(15, 2);

  private final Map<String, Integer> modifierMap = new HashMap<>();

  public ShelfService(AllocationService allocationService){
    this.allocationService = allocationService;
    modifierMap.put("hot", 1);
    modifierMap.put("cold", 1);
    modifierMap.put("frozen", 1);
    modifierMap.put("overflow", 2);
  }

  public void allocateShelf(CookedOrder order) {
    switch (order.getOrder().temp()){
      case "hot" -> tryPutOnHotShelf(order);
      case "cold" -> tryPutOnColdShelf(order);
      case "frozen" -> tryPutOnFrozenShelf(order);
      default -> log.warn("Unknown Shelf For Order!!!");
    }
  }

  public Optional<CookedOrder> collectOrder(String orderId, String allocation) {
    CookedOrder order = switch (allocation) {
      case "hot" -> getOrderFromShelf(orderId, hotShelf);
      case "cold" -> getOrderFromShelf(orderId, coldShelf);
      case "frozen" -> getOrderFromShelf(orderId, frozenShelf);
      case "overflow" -> getOrderFromShelf(orderId, overflowShelf);
      default -> null;
    };

    if (order != null && order.isFresh(modifierMap.get(allocation))){
      return Optional.of(order);
    }

    return Optional.empty();
  }

  public CookedOrder getOrderFromShelf(String orderId, AbstractShelf shelf){
    return shelf.removeOrder(orderId);
  }

  private void tryPutOnHotShelf(CookedOrder cookedOrder){
    if (hotShelf.putOrderIfThereIsSpace(cookedOrder)) {
      allocationService.putAllocation(cookedOrder.getOrder().id(), "hot");
    } else {
      overflowShelf.putOrderIfThereIsSpace(cookedOrder);
      allocationService.putAllocation(cookedOrder.getOrder().id(), "overflow");
    }
  }

  private void tryPutOnColdShelf(CookedOrder cookedOrder){
    if (coldShelf.putOrderIfThereIsSpace(cookedOrder)) {
      allocationService.putAllocation(cookedOrder.getOrder().id(), "cold");
    } else {
      overflowShelf.putOrderIfThereIsSpace(cookedOrder);
      allocationService.putAllocation(cookedOrder.getOrder().id(), "overflow");
    }
  }

  private void tryPutOnFrozenShelf(CookedOrder cookedOrder){
    if (frozenShelf.putOrderIfThereIsSpace(cookedOrder)) {
      allocationService.putAllocation(cookedOrder.getOrder().id(), "frozen");
    } else {
      overflowShelf.putOrderIfThereIsSpace(cookedOrder);
      allocationService.putAllocation(cookedOrder.getOrder().id(), "overflow");
    }
  }

}
