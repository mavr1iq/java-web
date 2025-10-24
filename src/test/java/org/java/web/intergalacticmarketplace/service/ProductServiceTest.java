package org.java.web.intergalacticmarketplace.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;

import org.java.web.intergalacticmarketplace.config.MappersTestConfig;
import org.java.web.intergalacticmarketplace.domain.Product;
import org.java.web.intergalacticmarketplace.exceptions.ProductNotFoundException;
import org.java.web.intergalacticmarketplace.service.impl.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.MethodArgumentNotValidException;

@SpringBootTest(classes = ProductServiceImpl.class)
@Import(MappersTestConfig.class)
@DisplayName("Product Service Tests")
@TestMethodOrder(OrderAnnotation.class)
public class ProductServiceTest {
    private static final String PRODUCT_NAME = "CosmicTestName";
    private static final String PRODUCT_DESCRIPTION = "testDescription";
    private static final double PRODUCT_PRICE = 123.45;

    @Autowired
    private ProductService productService;

    @Test
    @Order(1)
    @DisplayName("Get all products test")
    public void shouldGetAllProducts() {
        List<Product> allProducts = productService.getProducts();
        assertNotNull(allProducts);
        assertEquals(allProducts.size(), 2);
        assertIterableEquals(
                allProducts.stream().map(Product::getName).collect(Collectors.toList()),
                new ArrayList<>(List.of("Cosmic milk", "Galaxy mouse on stick")));
    }

    @Test
    @Order(2)
    @DisplayName("Get by id test")
    public void shouldGetProductById() {
        Product product = productService.getProductById(1L);
        assertThatNoException().isThrownBy(() -> productService.getProductById(1L));
        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Cosmic milk", product.getName());
        assertEquals("Milk from space cows", product.getDescription());
        assertEquals(80.5, product.getPrice());
    }

    @Test
    @Order(3)
    @DisplayName("Throwing exception from get by id test")
    public void shouldThrowExceptionGetProductById() {
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(35L));
    }

    @Test
    @Order(4)
    @DisplayName("Create product test")
    public void shouldCreateProduct() {
        Product newProduct = buildProduct(3L);

        Product response = productService.createProduct(newProduct);
        assertThatNoException().isThrownBy(() -> productService.createProduct(newProduct));
        assertEquals(newProduct, response);

        Product queried = productService.getProductById(3L);
        assertEquals(newProduct, queried);
    }

    @Test
    @Order(5)
    @DisplayName("Update product test")
    public void shouldUpdateProduct() {
        Product newProduct = buildProduct(1L);

        Product response = productService.updateProduct(1L, newProduct);
        assertThatNoException().isThrownBy(() -> productService.updateProduct(1L, newProduct));
        assertEquals(newProduct, response);

        Product queried = productService.getProductById(1L);
        assertEquals(newProduct, queried);
    }

    @Test
    @Order(6)
    @DisplayName("Throwing exception from update product test")
    public void shouldThrowExceptionUpdateProduct() {
        Product product = buildProduct(1000L);
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1000L, product));
    }

    @Test
    @Order(7)
    @DisplayName("Delete product test")
    public void shouldDeleteProduct() {
        assertDoesNotThrow(() -> productService.deleteProduct(1L));

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    private static Product buildProduct(Long id) {
        return Product.builder()
                .id(id)
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .build();
    }
}
