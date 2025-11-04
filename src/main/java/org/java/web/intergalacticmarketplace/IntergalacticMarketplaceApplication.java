package org.java.web.intergalacticmarketplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class IntergalacticMarketplaceApplication {

  public static void main(String[] args) {
    SpringApplication.run(IntergalacticMarketplaceApplication.class, args);
  }
}
