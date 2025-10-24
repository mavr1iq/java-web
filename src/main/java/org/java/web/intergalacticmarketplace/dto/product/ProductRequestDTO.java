package org.java.web.intergalacticmarketplace.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import org.java.web.intergalacticmarketplace.dto.validation.CosmicWordCheck;

@Value
@Schema(description = "Data for actions with products")
@Builder(toBuilder = true)
public class ProductRequestDTO {
  @Size(min = 3, max = 100, message = "Name must be greater than 3 and lower than 100 symbols")
  @CosmicWordCheck
  @Schema(description = "Product name")
  String name;

  @NotNull
  @PositiveOrZero(message = "Price must be greater or equal zero")
  @Schema(description = "Product price")
  double price;

  @Size(max = 300, message = "Must not be greater than 300 symbols")
  @Schema(description = "Product description")
  String description;
}
