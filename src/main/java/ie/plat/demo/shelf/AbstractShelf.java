package ie.plat.demo.shelf;

import ie.plat.demo.order.CookedOrder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractShelf {

  private final Integer maxOrdersOnShelf;

  private final Map<String, CookedOrder> shelf = new ConcurrentHashMap<>();

  private final Integer shelfModifier;

  protected AbstractShelf(Integer maxOrdersOnShelf, Integer shelfModifier) {
    this.maxOrdersOnShelf = maxOrdersOnShelf;
    this.shelfModifier = shelfModifier;
  }

  public synchronized CookedOrder removeOrder(String orderId) {
    return shelf.remove(orderId);
  }

  public synchronized boolean putOrderIfThereIsSpace(CookedOrder order){
    if(shelf.size() <= maxOrdersOnShelf){
      shelf.put(order.getOrder().id(), order);
      return true;
    }

    return false;
  }

  public synchronized void cleanShelf(String orderId) {
    shelf.entrySet().removeIf(entry -> !entry.getValue().isFresh(shelfModifier));
  }

}
