package br.com.gerencimentodepedidos.data.dto.request;

import br.com.gerencimentodepedidos.enums.ProductCategory;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductRequestDTO {
    private Long id;

    @NotBlank(message = "The product name is mandatory")
    @Size(min = 3, max = 80)
    private String name;

    @NotNull(message = "The value is mandatory")
    @DecimalMin(value = "0.1", message = "The value must be greater than zero")
    private double price;

    @NotNull(message = "The category is mandatory")
    private ProductCategory category;
}
