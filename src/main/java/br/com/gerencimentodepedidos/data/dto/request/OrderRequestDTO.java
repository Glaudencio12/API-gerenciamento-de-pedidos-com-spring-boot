package br.com.gerencimentodepedidos.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OrderRequestDTO {
    private Long id;

    @Schema(name = "items", defaultValue = "[]")
    private List<OrderItemRequestDTO> items = new ArrayList<>();

    @Schema(name = "full_value", example = "0.0")
    @NotNull(message = "The total value is mandatory starting with 0.0")
    @DecimalMax(value = "0.0", message = "The maximum value of entered is 0.0")
    @DecimalMin(value = "0.0", message = "The minimum value of entered is 0.0")
    private double fullValue;
}
