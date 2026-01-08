package br.com.gerencimentodepedidos.data.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({
        "id",
        "quantity",
        "product",
        "links"
})
public class OrderItemResponseDTO extends RepresentationModel<OrderItemResponseDTO> {
    private Long id;
    private ProductResponseDTO product;
    private int quantity;

    @Override
    @Schema(hidden = true)
    public Links getLinks() {
        return super.getLinks();
    }
}
