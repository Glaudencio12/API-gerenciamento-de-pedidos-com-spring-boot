package br.com.gerencimentodepedidos.data.dto.request;

import br.com.gerencimentodepedidos.model.OrderItem;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OrderRequestDTO {
    private Long id;

    private List<OrderItem> items = new ArrayList<>();

    @NotNull(message = "The total value is mandatory starting with 0.0")
    @Max(value = 0, message = "The maximum value of entered is 0.0")
    @DecimalMin(value = "0.0", message = "The minimum value of entered is 0.0")
    private double fullValue;
}
