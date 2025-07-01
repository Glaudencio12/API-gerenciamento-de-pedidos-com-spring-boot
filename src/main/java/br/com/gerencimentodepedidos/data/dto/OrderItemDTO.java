package br.com.gerencimentodepedidos.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "order")
public class OrderItemDTO extends RepresentationModel<OrderItemDTO> {
    private Long id;
    private ProductDTO product;
    private int quantity;
    @JsonIgnore
    private OrderDTO order;
}
