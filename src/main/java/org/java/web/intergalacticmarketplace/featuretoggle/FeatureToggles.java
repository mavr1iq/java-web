package org.java.web.intergalacticmarketplace.featuretoggle;

import lombok.Getter;

@Getter
public enum FeatureToggles {
    HALLOWEEN_TOGGLE("halloween-toggle");

    private final String featureName;

    FeatureToggles(String featureName) { this.featureName = featureName; }
}
