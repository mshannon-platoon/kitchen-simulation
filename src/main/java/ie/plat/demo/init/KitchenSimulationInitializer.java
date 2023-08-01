package ie.plat.demo.init;

import ie.plat.demo.order.OrderProcessService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class KitchenSimulationInitializer implements InitializingBean {

  private final OrderProcessService orderImporter;

  public KitchenSimulationInitializer(OrderProcessService orderImporter){
    this.orderImporter = orderImporter;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    orderImporter.processOrders();
  }
}
