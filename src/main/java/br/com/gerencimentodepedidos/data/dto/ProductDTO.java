package br.com.gerencimentodepedidos.data.dto;

import br.com.gerencimentodepedidos.enums.ProductCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    @JsonProperty("category_product")
    private ProductCategory category;
}
