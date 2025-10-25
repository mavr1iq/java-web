package org.java.web.intergalacticmarketplace.featuretoggle;

import lombok.Getter;

@Getter
public enum FeatureToggles {
    HALLOWEEN_TOGGLE("halloween");

    private final String featureName;

    FeatureToggles(String featureName) { this.featureName = featureName; }
}
