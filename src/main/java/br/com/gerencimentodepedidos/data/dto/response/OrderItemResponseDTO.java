package br.com.gerencimentodepedidos.data.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@Getter
@Setter
public class OrderItemResponseDTO extends RepresentationModel<OrderItemResponseDTO> {
    private Long id;
    private ProductResponseDTO product;
    private int quantity;
}
