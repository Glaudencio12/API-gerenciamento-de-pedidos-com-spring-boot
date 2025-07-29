package br.com.gerencimentodepedidos.data.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class OrderItemRequestDTO {
    private Long id;

    @NotNull(message = "The product is mandatory")
    @Min(value = 1, message = "There is no zero id in the database")
    private Long productId;

    @NotNull(message = "The quantity is mandatory")
    @Min(value = 1, message = "The minimum quantity is 1")
    private int quantity;

    @NotNull(message = "The id of the order is mandatory")
    @Min(value = 1, message = "There is no zero id in the database")
    private Long orderId;
}
