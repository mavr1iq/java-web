package org.java.web.intergalacticmarketplace.featuretoggle;

import org.java.web.intergalacticmarketplace.config.FeatureToggleProperties;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;


@Service
public class FeatureToggleService {

    private final ConcurrentHashMap<String, Boolean> featureToggles;

    public FeatureToggleService(FeatureToggleProperties featureToggleProperties) {
        featureToggles = new ConcurrentHashMap<>(featureToggleProperties.getToggles());
    }

    public void enable(String feature) {
        featureToggles.put(feature, true);
    }

    public void disable(String feature) {
        featureToggles.put(feature, false);
    }

    public boolean isEnabled(String feature) {
        return featureToggles.getOrDefault(feature, false);
    }
}
