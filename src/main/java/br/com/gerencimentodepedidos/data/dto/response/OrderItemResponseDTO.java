package br.com.gerencimentodepedidos.data.dto.response;

import br.com.gerencimentodepedidos.model.OrderItem;
import br.com.gerencimentodepedidos.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@Getter
@Setter
public class OrderItemResponseDTO extends RepresentationModel<OrderResponseDTO> {
    private Long id;
    private Product product;
    private int quantity;
    @JsonIgnore
    private OrderItem order;
}
