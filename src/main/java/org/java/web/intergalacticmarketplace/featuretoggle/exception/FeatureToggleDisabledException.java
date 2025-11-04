package org.java.web.intergalacticmarketplace.featuretoggle.exception;

public class FeatureToggleDisabledException extends RuntimeException {
    private static final String FEATURE_DISABLED_MESSAGE = "Feature with name %s is disabled";

    public FeatureToggleDisabledException(String featureName) {
        super(String.format(FEATURE_DISABLED_MESSAGE, featureName));
    }
}
