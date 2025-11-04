package org.java.web.intergalacticmarketplace.web;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.java.web.intergalacticmarketplace.AbstractIT;
import org.java.web.intergalacticmarketplace.config.MappersTestConfig;
import org.java.web.intergalacticmarketplace.domain.Product;
import org.java.web.intergalacticmarketplace.dto.product.ProductRequestDTO;
import org.java.web.intergalacticmarketplace.exceptions.GlobalExceptionHandler;
import org.java.web.intergalacticmarketplace.exceptions.ProductNotFoundException;
import org.java.web.intergalacticmarketplace.service.ProductService;
import org.java.web.intergalacticmarketplace.web.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.net.URI;
import java.util.List;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
@Import({MappersTestConfig.class, GlobalExceptionHandler.class})
@DisplayName("Product Controller IT")
@Tag("product-service")
public class ProductControllerIT extends AbstractIT {
    private static final String PRODUCT_NAME = "Cosmic Milk";
    private static final String PRODUCT_DESCRIPTION = "Milk from space cows";
    private static final double PRODUCT_PRICE = 80.5;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        reset(productService);
    }

    private static ProductRequestDTO buildProductRequestDTO() {
        return ProductRequestDTO.builder()
                .name(PRODUCT_NAME)
                .price(PRODUCT_PRICE)
                .description(PRODUCT_DESCRIPTION)
                .build();
    }

    private static ProductRequestDTO buildExceptionProductRequestDTO() {
        return ProductRequestDTO.builder()
                .name("")
                .price(PRODUCT_PRICE)
                .description(PRODUCT_DESCRIPTION)
                .build();
    }

    @Test
    @SneakyThrows
    void shouldGetAllProducts() {
        ProductRequestDTO productRequestDTO = buildProductRequestDTO();
        Product product = productMapper.toProductEntity(productRequestDTO);

        when(productService.getProducts()).thenReturn(List.of(product));

        mockMvc
                .perform(get("/api/v1/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$[0].description").value(PRODUCT_DESCRIPTION))
                .andExpect(jsonPath("$[0].price").value(PRODUCT_PRICE));

        verify(productService, times(1)).getProducts();
    }

    @Test
    @SneakyThrows
    void shouldGetProductById() {
        ProductRequestDTO productRequestDTO = buildProductRequestDTO();
        Product product = productMapper.toProductEntity(productRequestDTO);

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc
                .perform(get("/api/v1/products/{id}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.description").value(PRODUCT_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(PRODUCT_PRICE));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    @SneakyThrows
    void shouldThrowExceptionProductById() {
        Long id = 1000L;
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(NOT_FOUND, String.format("Product with id %s not found", id));

        problemDetail.setTitle("Product Not Found");
        problemDetail.setType(URI.create("/errors/product-not-found"));

        when(productService.getProductById(id)).thenThrow(new ProductNotFoundException(id));

        mockMvc
                .perform(get("/api/v1/products/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));

        verify(productService, times(1)).getProductById(id);
    }

    @Test
    @SneakyThrows
    void shouldCreateProduct() {
        ProductRequestDTO productRequestDTO = buildProductRequestDTO();
        Product product = productMapper.toProductEntity(productRequestDTO);

        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc
                .perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.description").value(PRODUCT_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(PRODUCT_PRICE));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    @SneakyThrows
    void shouldThrowExceptionCreateProduct() {
        ProductRequestDTO productRequestDTO = buildExceptionProductRequestDTO();
        Product product = productMapper.toProductEntity(productRequestDTO);

        when(productService.createProduct(product)).thenReturn(null);

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(BAD_REQUEST,
                        "Validation failed for object: productRequestDTO. Field: name - Name must be greater than 3 and lower than 100 symbols");

        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create("/errors/validation-error"));

        mockMvc
                .perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));

        verify(productService, never()).createProduct(product);
    }

    @Test
    @SneakyThrows
    void shouldUpdateProduct() {
        Long id = 1L;
        ProductRequestDTO productRequestDTO = buildProductRequestDTO();
        Product product = productMapper.toProductEntity(productRequestDTO);

        when(productService.updateProduct(eq(id), any(Product.class))).thenReturn(product);

        mockMvc
                .perform(put("/api/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.description").value(PRODUCT_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(PRODUCT_PRICE));

        verify(productService, times(1)).updateProduct(id, product);
    }

    @Test
    @SneakyThrows
    void shouldThrowExceptionNotFoundUpdateProduct() {
        Long id = 1000L;
        ProductRequestDTO productRequestDTO = buildProductRequestDTO();
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(NOT_FOUND, String.format("Product with id %s not found", id));

        problemDetail.setTitle("Product Not Found");
        problemDetail.setType(URI.create("/errors/product-not-found"));

    when(productService.updateProduct(eq(id), any(Product.class)))
        .thenThrow(new ProductNotFoundException(id));

        mockMvc
                .perform(put("/api/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));

        verify(productService, times(1)).updateProduct(eq(id), any(Product.class));
    }

    @Test
    @SneakyThrows
    void shouldThrowValidationExceptionUpdateProduct() {
        Long id = 1L;
        ProductRequestDTO productRequestDTO = buildExceptionProductRequestDTO();
        Product product = productMapper.toProductEntity(productRequestDTO);
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(BAD_REQUEST,
                        "Validation failed for object: productRequestDTO. Field: name - Name must be greater than 3 and lower than 100 symbols");

        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create("/errors/validation-error"));

        when(productService.updateProduct(id, product)).thenReturn(null);

        mockMvc
                .perform(put("/api/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));

        verify(productService, never()).updateProduct(id, product);
    }

    @Test
    @SneakyThrows
    void shouldDeleteProduct() {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/{id}", 1L)).andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }
}
