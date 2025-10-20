package org.java.web.intergalacticmarketplace.domain;

import lombok.Data;

@Data
public class Product {
  Long id;
  String name;
  double price;
  String description;
}
