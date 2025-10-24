package org.java.web.intergalacticmarketplace.service;

import org.java.web.intergalacticmarketplace.domain.Product;
import org.java.web.intergalacticmarketplace.dto.product.ProductDTO;
import org.java.web.intergalacticmarketplace.dto.product.ProductRequestDTO;

import java.util.List;

public interface ProductService {
  List<Product> getProducts();

  Product getProductById(Long id);

  Product createProduct(Product product);

  Product updateProduct(Long id, Product product);

  void deleteProduct(Long id);
}
