package br.com.gerencimentodepedidos.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OrderDTO extends RepresentationModel<OrderDTO> {
    private Long id;
    private List<OrderItemDTO> items = new ArrayList<>();
    @JsonProperty("full_value")
    private double fullValue;
}
