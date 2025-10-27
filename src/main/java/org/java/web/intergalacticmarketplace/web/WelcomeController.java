package org.java.web.intergalacticmarketplace.web;

import lombok.RequiredArgsConstructor;
import org.java.web.intergalacticmarketplace.config.WelcomeProperties;
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
@RequestMapping("/api/v1/welcome")
@RequiredArgsConstructor
@Validated
public class WelcomeController {
    private final WelcomeProperties welcomeProperties;


    @GetMapping
@FeatureToggle(FeatureToggles.WELCOME_TOGGLE)
    public ResponseEntity<String> getWelcomed() {
        String name = getName();
        return ResponseEntity.ok(String.format("Welcome to %s", name));
    }


    public String getName() {
        return String.format("Spooky %s", welcomeProperties.getName());
    }
}
