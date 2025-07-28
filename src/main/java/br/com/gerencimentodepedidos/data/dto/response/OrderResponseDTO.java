package br.com.gerencimentodepedidos.data.dto.response;

import br.com.gerencimentodepedidos.model.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OrderResponseDTO extends RepresentationModel<OrderResponseDTO> {
    private Long id;
    private List<OrderItem> items = new ArrayList<>();
    @JsonProperty("full_value")
    private double fullValue;
}
