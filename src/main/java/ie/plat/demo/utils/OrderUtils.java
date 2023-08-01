package ie.plat.demo.utils;

import ie.plat.demo.order.CookedOrder;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderUtils {

  public String findOldestOrderOnShelf(List<CookedOrder> cookedOrderList, Integer shelfModifier) {
    List<CookedOrder> copyList = new ArrayList<>(cookedOrderList);
    boolean sorted = false;
    CookedOrder temp;
    while (!sorted) {
      sorted = true;
      for (int i = 0; i < copyList.size() - 1; i++) {
        if (copyList.get(i).getExpiryValue(shelfModifier) >
            copyList.get(i + 1).getExpiryValue(shelfModifier)) {

          temp = copyList.get(i);
          copyList.set(i, copyList.get(i + 1));
          copyList.set(i + 1, temp);
          sorted = false;
        }
      }
    }

    return copyList.get(0).getOrder().id();
  }

}
