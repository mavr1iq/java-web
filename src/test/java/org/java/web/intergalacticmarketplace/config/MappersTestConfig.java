package org.java.web.intergalacticmarketplace.config;

import org.java.web.intergalacticmarketplace.web.mapper.ProductMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MappersTestConfig {
    @Bean
    public ProductMapper productMapper() {
        return Mappers.getMapper(ProductMapper.class);
    }
}
