package org.java.web.intergalacticmarketplace.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Product {
  Long id;
  String name;
  double price;
  String description;
}
