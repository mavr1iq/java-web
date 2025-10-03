package org.java.web.intergalacticmarketplace.dto.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CosmicWordValidator.class)
public @interface CosmicWordCheck {
    String message() default "Product name must contain a cosmic word)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}