package org.java.web.intergalacticmarketplace.featuretoggle;

import lombok.Getter;

@Getter
public enum FeatureToggles {
    WELCOME_TOGGLE("welcome");

    private final String featureName;

    FeatureToggles(String featureName) { this.featureName = featureName; }
}
