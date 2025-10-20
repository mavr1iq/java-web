package org.java.web.intergalacticmarketplace.domain;

import lombok.Value;

import java.util.List;

@Value
public class Order {
  Long id;
  List<Product> products;
}
