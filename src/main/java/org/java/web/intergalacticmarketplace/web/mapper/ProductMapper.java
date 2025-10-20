package org.java.web.intergalacticmarketplace.web.mapper;

import org.java.web.intergalacticmarketplace.domain.Product;
import org.java.web.intergalacticmarketplace.dto.product.ProductDTO;
import org.java.web.intergalacticmarketplace.dto.product.ProductRequestDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  ProductDTO toProductDTO(Product product);

  List<ProductDTO> toProductList(List<Product> products);

  Product toProductEntity(ProductRequestDTO request);

  Product toProductEntity(ProductDTO productDTO);
}
