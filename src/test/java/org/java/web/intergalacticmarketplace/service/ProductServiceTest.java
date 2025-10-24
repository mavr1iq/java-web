package org.java.web.intergalacticmarketplace.service;

import org.java.web.intergalacticmarketplace.config.MappersTestConfig;
import org.java.web.intergalacticmarketplace.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(classes = ProductServiceImpl.class)
@Import(MappersTestConfig.class)
@DisplayName("Product Service Tests")
@TestMethodOrder(OrderAnnotation.class)
public class ProductServiceTest {
    private static final String name = "test";
    private static final String description = "test123";
    private static final double price = 123.45;

    @Autowired
    private ProductServiceImpl productService;
}
