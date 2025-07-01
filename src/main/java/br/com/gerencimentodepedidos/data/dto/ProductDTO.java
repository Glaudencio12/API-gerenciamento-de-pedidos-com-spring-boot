package br.com.gerencimentodepedidos.data.dto;

import br.com.gerencimentodepedidos.enums.ProductCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@Getter
@Setter
public class ProductDTO extends RepresentationModel<ProductDTO> {
    private Long id;
    private String name;
    private double price;
    @JsonProperty("category_product")
    private ProductCategory category;
}
