package org.java.web.intergalacticmarketplace.feature;

import org.java.web.intergalacticmarketplace.featuretoggle.FeatureToggleService;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class FeatureExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {
            FeatureToggleService toggleService = SpringExtension.getApplicationContext(context)
                    .getBean(FeatureToggleService.class);

            System.out.println(method.isAnnotationPresent(DisableFeature.class));

            if (method.isAnnotationPresent(EnableFeature.class)) {
                String featureName = method.getAnnotation(EnableFeature.class).value().getFeatureName();
                toggleService.enable(featureName);

            } else if (method.isAnnotationPresent(DisableFeature.class)) {
                String featureName = method.getAnnotation(DisableFeature.class).value().getFeatureName();
                toggleService.disable(featureName);
            }
        });
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        context.getTestMethod().ifPresent(method -> {
            FeatureToggleService toggleService = SpringExtension.getApplicationContext(context)
                    .getBean(FeatureToggleService.class);
            Environment env = SpringExtension.getApplicationContext(context).getEnvironment();
            String featureName = null;

            if (method.isAnnotationPresent(EnableFeature.class)) {
                featureName = method.getAnnotation(EnableFeature.class).value().getFeatureName();
            } else if (method.isAnnotationPresent(DisableFeature.class)) {
                featureName = method.getAnnotation(DisableFeature.class).value().getFeatureName();
            }

            if (env.getProperty(String.format("feature.toggles.%s", featureName), Boolean.class)) {
                toggleService.enable(featureName);
            } else {
                toggleService.disable(featureName);
            }
        });
    }
}
