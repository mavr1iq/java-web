package org.java.web.intergalacticmarketplace.feature;


import org.java.web.intergalacticmarketplace.featuretoggle.FeatureToggleService;
import org.java.web.intergalacticmarketplace.featuretoggle.FeatureToggles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DisableFeature {

    FeatureToggles value();

}
