package br.com.gerencimentodepedidos.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderItemDTO {
    private Long id;
    private ProductDTO product;
    private int quantity;
    @JsonIgnore
    private OrderDTO order;
}
