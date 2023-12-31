package ie.plat.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class KitchenSimulationApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(KitchenSimulationApplication.class)
        .web(WebApplicationType.NONE)
        .run(args);
  }

}
