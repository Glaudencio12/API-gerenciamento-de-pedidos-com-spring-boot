package br.com.gerencimentodepedidos.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({
        "id",
        "full_value",
        "items",
        "links"
})
public class OrderResponseDTO extends RepresentationModel<OrderResponseDTO> {
    private Long id;
    private List<OrderItemResponseDTO> items = new ArrayList<>();
    @JsonProperty("full_value")
    private double fullValue;

    @Override
    @Schema(hidden = true)
    public Links getLinks() {
        return super.getLinks();
    }
}
