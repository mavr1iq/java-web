package org.java.web.intergalacticmarketplace.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.java.web.intergalacticmarketplace.domain.Product;
import org.java.web.intergalacticmarketplace.exceptions.ProductNotFoundException;
import org.java.web.intergalacticmarketplace.service.ProductService;
import org.java.web.intergalacticmarketplace.web.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductMapper productMapper;
  private final Map<Long, Product> products = new ConcurrentHashMap<>();
  private final AtomicLong id = new AtomicLong(0);

  @PostConstruct
  public void init() {
    createProduct(Product.builder().name("Cosmic milk").price(80.5).description("Milk from space cows").build());
    createProduct(Product.builder().name("Galaxy mouse on stick").price(80.5).description("Fake mouse on stick but... from galaxy").build());
  }

  @Override
  public List<Product> getProducts() {
    return products.values().stream().toList();
  }

  @Override
  public Product getProductById(Long id) {
    if (!products.containsKey(id)) {
      throw new ProductNotFoundException(id);
    }
    return products.get(id);
  }

  @Override
  public Product createProduct(Product product) {
    product.setId(id.incrementAndGet());
    products.put(product.getId(), product);
    return product;
  }

  @Override
  public Product updateProduct(Long id, Product product) {
    Product updateProduct = products.get(id);
    if (!products.containsKey(id)) {
      throw new ProductNotFoundException(id);
    }

    updateProduct.setName(product.getName());
    updateProduct.setPrice(product.getPrice());
    updateProduct.setDescription(product.getDescription());

    products.put(id, updateProduct);
    return updateProduct;
  }

  @Override
  public void deleteProduct(Long id) {
    products.remove(id);
  }
}
