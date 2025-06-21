package br.com.gerencimentodepedidos.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@Data
@ToString(exclude = "order")
public class OrderItemDTO extends RepresentationModel<OrderItemDTO> {
    private Long id;
    private ProductDTO product;
    private int quantity;
    @JsonIgnore
    private OrderDTO order;
}
