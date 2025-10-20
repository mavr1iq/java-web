package org.java.web.intergalacticmarketplace.domain;

import lombok.Value;

import java.util.List;

@Value
public class Cart {
  Long id;
  List<Product> items;
}
