package br.com.gerencimentodepedidos.data.dto.request;

import br.com.gerencimentodepedidos.enums.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductRequestDTO {
    private Long id;

    @Schema(name = "name", example = "Carne assada")
    @NotBlank(message = "The product name is mandatory")
    @Size(min = 3, max = 80)
    private String name;

    @Schema(name = "price", example = "12.00")
    @NotNull(message = "The value is mandatory")
    @DecimalMin(value = "5.00", message = "The minimum price for the price is 5")
    private double price;

    @Schema(name = "category", example = "PRATO_PRINCIPAL")
    @NotNull(message = "The category is mandatory")
    private ProductCategory category;
}
