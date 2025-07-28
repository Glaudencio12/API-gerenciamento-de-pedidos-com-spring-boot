package br.com.gerencimentodepedidos.data.dto.request;

import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.model.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class OrderItemRequestDTO {
    private Long id;
    @NotNull(message = "The product is mandatory")
    private Product product;

    @NotNull(message = "The quantity is mandatory")
    @Min(value = 1, message = "The minimum quantity is 1")
    private int quantity;

    @NotNull(message = "The order is mandatory")
    private Order order;
}
