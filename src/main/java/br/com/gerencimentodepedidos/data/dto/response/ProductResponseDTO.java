package br.com.gerencimentodepedidos.data.dto.response;

import br.com.gerencimentodepedidos.enums.ProductCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@Getter
@Setter
public class ProductResponseDTO extends RepresentationModel<ProductResponseDTO> {
    private Long id;
    private String name;
    private double price;
    private ProductCategory category;
}
