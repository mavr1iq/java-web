package org.java.web.intergalacticmarketplace.web;

import lombok.SneakyThrows;
import org.java.web.intergalacticmarketplace.AbstractIT;
import org.java.web.intergalacticmarketplace.config.WelcomeProperties;
import org.java.web.intergalacticmarketplace.exceptions.GlobalExceptionHandler;
import org.java.web.intergalacticmarketplace.feature.DisableFeature;
import org.java.web.intergalacticmarketplace.feature.EnableFeature;
import org.java.web.intergalacticmarketplace.featuretoggle.FeatureToggles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WelcomeController.class)
@AutoConfigureMockMvc
@Import({GlobalExceptionHandler.class, WelcomeProperties.class})
@DisplayName("Welcome feature Controller IT")
@ExtendWith(SpringExtension.class)
@Tag("feature-service")
public class WelcomeControllerIT extends AbstractIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    @EnableFeature(FeatureToggles.WELCOME_TOGGLE)
    void shouldGetWelcome() {
        mockMvc
                .perform(get("/api/v1/welcome").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @DisableFeature(FeatureToggles.WELCOME_TOGGLE)
    void shouldGetFeatureDisabledException() {
        mockMvc
                .perform(get("/api/v1/halloween").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
