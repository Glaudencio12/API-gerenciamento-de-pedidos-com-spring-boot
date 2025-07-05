package br.com.gerencimentodepedidos.data.dto;

import br.com.gerencimentodepedidos.enums.ProductCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@Getter
@Setter
public class ProductDTO extends RepresentationModel<ProductDTO> {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private double price;
    @NotNull
    @JsonProperty("category_product")
    private ProductCategory category;
}
