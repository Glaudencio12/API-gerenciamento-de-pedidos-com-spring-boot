package br.com.gerencimentodepedidos.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@Getter
@Setter
public class OrderItemDTO extends RepresentationModel<OrderItemDTO> {
    private Long id;
    @NotNull
    private ProductDTO product;
    @NotNull
    private int quantity;
    @JsonIgnore
    @NotNull
    private OrderDTO order;
}
