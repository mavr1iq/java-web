package org.java.web.intergalacticmarketplace.dto.product;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ProductDTO {
  int id;
  String name;
  double price;
  String description;
}
