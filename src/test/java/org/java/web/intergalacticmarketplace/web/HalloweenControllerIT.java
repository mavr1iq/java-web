package org.java.web.intergalacticmarketplace.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.java.web.intergalacticmarketplace.AbstractIT;
import org.java.web.intergalacticmarketplace.config.MappersTestConfig;
import org.java.web.intergalacticmarketplace.domain.Product;
import org.java.web.intergalacticmarketplace.dto.product.ProductRequestDTO;
import org.java.web.intergalacticmarketplace.exceptions.GlobalExceptionHandler;
import org.java.web.intergalacticmarketplace.feature.DisableFeature;
import org.java.web.intergalacticmarketplace.feature.EnableFeature;
import org.java.web.intergalacticmarketplace.feature.FeatureExtension;
import org.java.web.intergalacticmarketplace.featuretoggle.FeatureToggleService;
import org.java.web.intergalacticmarketplace.featuretoggle.FeatureToggles;
import org.java.web.intergalacticmarketplace.service.ProductService;
import org.java.web.intergalacticmarketplace.web.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HalloweenController.class)
@AutoConfigureMockMvc
@Import({MappersTestConfig.class, GlobalExceptionHandler.class})
@DisplayName("Halloween feature Controller IT")
@ExtendWith(FeatureExtension.class)
@Tag("feature-service")
public class HalloweenControllerIT extends AbstractIT {
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

    @MockitoBean
    FeatureToggleService featureToggleService;

    @BeforeEach
    void setUp() {
        reset(productService, featureToggleService);
    }

    private static ProductRequestDTO buildProductRequestDTO() {
        return ProductRequestDTO.builder()
                .name(PRODUCT_NAME)
                .price(PRODUCT_PRICE)
                .description(PRODUCT_DESCRIPTION)
                .build();
    }

    @Test
    @SneakyThrows
    @EnableFeature(FeatureToggles.HALLOWEEN_TOGGLE)
    void shouldGetAllProducts() {
        ProductRequestDTO productRequestDTO = buildProductRequestDTO();
        Product product = productMapper.toProductEntity(productRequestDTO);

        when(productService.getProducts()).thenReturn(List.of(product));

        mockMvc
                .perform(get("/api/v1/halloween").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                        .value(String.format("Spooky %s", PRODUCT_NAME)))
                .andExpect(jsonPath("$[0].description").value(PRODUCT_DESCRIPTION))
                .andExpect(jsonPath("$[0].price").value(PRODUCT_PRICE));

        verify(productService, times(1)).getProducts();
    }

    @Test
    @SneakyThrows
    @DisableFeature(FeatureToggles.HALLOWEEN_TOGGLE)
    void shouldGetFeatureDisabledException() {
        ProductRequestDTO productRequestDTO = buildProductRequestDTO();
        Product product = productMapper.toProductEntity(productRequestDTO);

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(NOT_FOUND, String.format("Feature with name %s is disabled",
                        FeatureToggles.HALLOWEEN_TOGGLE));

        problemDetail.setTitle("Feature is disabled");
        problemDetail.setType(URI.create("/errors/feature-toggle-disabled"));



        when(productService.getProducts()).thenReturn(List.of(product));

        mockMvc
                .perform(get("/api/v1/halloween").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(problemDetail)));

        verify(productService, times(1)).getProducts();
    }
}
