package org.java.web.intergalacticmarketplace.featuretoggle.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.java.web.intergalacticmarketplace.featuretoggle.FeatureToggleService;
import org.java.web.intergalacticmarketplace.featuretoggle.FeatureToggles;
import org.java.web.intergalacticmarketplace.featuretoggle.annotation.FeatureToggle;
import org.java.web.intergalacticmarketplace.featuretoggle.exception.FeatureToggleDisabledException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FeatureToggleAspect {
    private final FeatureToggleService featureToggleService;

    @Around(value = "@annotation(featureToggle)")
    public Object checkToggle(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
        FeatureToggles featureToggles = featureToggle.value();
        System.out.println("test");
        if(featureToggleService.isEnabled(featureToggles.getFeatureName())) {
            return joinPoint.proceed();
        }
        throw new FeatureToggleDisabledException(featureToggles.getFeatureName());
    }
}
