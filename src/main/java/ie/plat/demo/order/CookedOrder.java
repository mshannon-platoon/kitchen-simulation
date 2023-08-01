package ie.plat.demo.order;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CookedOrder {

  private Order order;
  private LocalDateTime cookedDateTime;

  public boolean isFresh(Integer shelfModifier) {
    return (order.shelfLife() - order.decayRate() * getAge() * shelfModifier) / order.shelfLife()
        > 0.0f;
  }

  public float getExpiryValue(Integer shelfModifier){
    return (order.shelfLife() - order.decayRate() * getAge() * shelfModifier) / order.shelfLife();
  }

  private long getAge() {
    return ChronoUnit.SECONDS.between(cookedDateTime, LocalDateTime.now());
  }
}
