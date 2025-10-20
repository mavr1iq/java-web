package org.java.web.intergalacticmarketplace.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class CosmicWordValidator implements ConstraintValidator<CosmicWordCheck, String> {
  private static final List<String> COSMIC_WORDS =
      Arrays.asList("galaxy", "star", "comet", "cosmic", "cosmo", "planet");

  @Override
  public boolean isValid(String text, ConstraintValidatorContext context) {
    if (text == null || text.trim().isEmpty()) {
      return true;
    }
    return COSMIC_WORDS.stream().anyMatch(text.toLowerCase()::contains);
  }
}
