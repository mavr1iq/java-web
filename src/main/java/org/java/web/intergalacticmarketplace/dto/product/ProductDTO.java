package org.java.web.intergalacticmarketplace.dto.product;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ProductDTO {
  Long id;
  String name;
  double price;
  String description;
}
