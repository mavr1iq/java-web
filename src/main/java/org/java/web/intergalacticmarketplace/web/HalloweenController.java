package org.java.web.intergalacticmarketplace.web;

import lombok.RequiredArgsConstructor;
import org.java.web.intergalacticmarketplace.dto.product.ProductDTO;
import org.java.web.intergalacticmarketplace.featuretoggle.FeatureToggles;
import org.java.web.intergalacticmarketplace.featuretoggle.annotation.FeatureToggle;
import org.java.web.intergalacticmarketplace.service.ProductService;
import org.java.web.intergalacticmarketplace.web.mapper.ProductMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/halloween")
@RequiredArgsConstructor
@Validated
public class HalloweenController {
    private final ProductService productService;
    private final ProductMapper productMapper;


    @GetMapping
    @FeatureToggle(FeatureToggles.HALLOWEEN_TOGGLE)
    public ResponseEntity<List<ProductDTO>> getProducts() {
        return ResponseEntity.ok(productMapper.toProductList(productService.getProducts().stream()
                .peek(p -> p.setName(String.format("Spooky %s", p.getName())))
                .collect(Collectors.toList())));
    }
}
