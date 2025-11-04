package org.java.web.intergalacticmarketplace.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.java.web.intergalacticmarketplace.dto.product.ProductDTO;
import org.java.web.intergalacticmarketplace.dto.product.ProductRequestDTO;
import org.java.web.intergalacticmarketplace.service.ProductService;
import org.java.web.intergalacticmarketplace.web.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Cosmic products manager")
public class ProductController {
  private final ProductService productService;
  private final ProductMapper productMapper;

  @Operation(summary = "Get list of products", description = "Retrieve all available products")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "200", description = "Retrieved all products")})
  @GetMapping
  public ResponseEntity<List<ProductDTO>> getProducts() {
    return ResponseEntity.ok(productMapper.toProductList(productService.getProducts()));
  }

  @Operation(summary = "Get product by id", description = "Retrieve existing product by id")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Retrieved product"),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  @GetMapping("/{id}")
  public ResponseEntity<ProductDTO> getProductById(
      @Parameter(description = "ID of product") @PathVariable Long id) {
    return ResponseEntity.ok(productMapper.toProductDTO(productService.getProductById(id)));
  }

  @Operation(summary = "Create product", description = "Create a new product for store")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Product created"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
      })
  @PostMapping
  public ResponseEntity<ProductDTO> createProduct(
      @Valid @RequestBody ProductRequestDTO requestDTO) {
    return new ResponseEntity<>(productMapper.toProductDTO(productService.createProduct(productMapper.toProductEntity(requestDTO))), HttpStatus.CREATED);
  }

  @Operation(summary = "Update product", description = "Update existing product by id")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Product updated"),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  @PutMapping("/{id}")
  public ResponseEntity<ProductDTO> updateProduct(
      @Parameter(description = "ID of product") @PathVariable Long id,
      @Valid @RequestBody ProductRequestDTO requestDTO) {
    return ResponseEntity.ok(productMapper.toProductDTO(productService.updateProduct(id, productMapper.toProductEntity(requestDTO))));
  }

  @Operation(summary = "Delete product", description = "Delete existing product by id")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Product deleted")})
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(
      @Parameter(description = "ID of product") @PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}
