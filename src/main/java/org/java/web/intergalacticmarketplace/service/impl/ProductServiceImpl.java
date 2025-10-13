package org.java.web.intergalacticmarketplace.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.java.web.intergalacticmarketplace.domain.Product;
import org.java.web.intergalacticmarketplace.dto.product.ProductDTO;
import org.java.web.intergalacticmarketplace.dto.product.ProductRequestDTO;
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
  private final Map<Integer, Product> products = new ConcurrentHashMap<>();
  private final AtomicLong id = new AtomicLong(0);

  @PostConstruct
  public void init() {
    createProduct(new ProductRequestDTO("Cosmic milk", 80.5, "Milk from space cows"));
    createProduct(
        new ProductRequestDTO(
            "Galaxy mouse on stick", 80.5, "Fake mouse on stick but... from galaxy"));
  }

  @Override
  public List<ProductDTO> getProducts() {
    return productMapper.toProductList(products.values().stream().toList());
  }

  @Override
  public ProductDTO getProductById(int id) {
    if (!products.containsKey(id)) {
      throw new ProductNotFoundException(id);
    }
    return productMapper.toProductDTO(products.get(id));
  }

  @Override
  public ProductDTO createProduct(ProductRequestDTO requestDTO) {
    Product newProduct = productMapper.toProductEntity(requestDTO);
    newProduct.setId((int) id.incrementAndGet());
    products.put(newProduct.getId(), newProduct);
    return productMapper.toProductDTO(newProduct);
  }

  @Override
  public ProductDTO updateProduct(int id, ProductRequestDTO requestDTO) {
    Product product = products.get(id);
    if (!products.containsKey(id)) {
      throw new ProductNotFoundException(id);
    }

    product.setName(requestDTO.getName());
    product.setPrice(requestDTO.getPrice());
    product.setDescription(requestDTO.getDescription());

    products.put(id, product);
    return productMapper.toProductDTO(product);
  }

  @Override
  public void deleteProduct(int id) {
    products.remove(id);
  }
}
