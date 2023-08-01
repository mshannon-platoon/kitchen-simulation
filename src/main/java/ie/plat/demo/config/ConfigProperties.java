package ie.plat.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "config")
@Getter
@Setter
public class ConfigProperties {

  private Integer ordersPerSecond;
  private String ordersFilename;

  private Integer scheduledThreadPoolSize = 3; // default


}
