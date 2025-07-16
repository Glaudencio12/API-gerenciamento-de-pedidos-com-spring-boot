package br.com.gerencimentodepedidos.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@Getter
@Setter
public class OrderItemDTO extends RepresentationModel<OrderItemDTO> {
    private Long id;
    @NotNull(message = "The product is mandatory")
    private ProductDTO product;

    @NotNull(message = "The quantity is mandatory")
    @Min(value = 1, message = "The minimum quantity is 1")
    private int quantity;

    @JsonIgnore
    @NotNull(message = "The order is mandatory")
    private OrderDTO order;
}
