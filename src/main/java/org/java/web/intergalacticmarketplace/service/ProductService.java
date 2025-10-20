package org.java.web.intergalacticmarketplace.service;

import org.java.web.intergalacticmarketplace.domain.Product;
import org.java.web.intergalacticmarketplace.dto.product.ProductDTO;
import org.java.web.intergalacticmarketplace.dto.product.ProductRequestDTO;

import java.util.List;

public interface ProductService {
  List<ProductDTO> getProducts();

  ProductDTO getProductById(Long id);

  ProductDTO createProduct(ProductRequestDTO requestDTO);

  ProductDTO updateProduct(Long id, ProductRequestDTO requestDTO);

  void deleteProduct(Long id);
}
