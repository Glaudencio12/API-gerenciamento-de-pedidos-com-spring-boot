package br.com.gerencimentodepedidos.controller;

import br.com.gerencimentodepedidos.controller.docs.OrderItemControllerDocs;
import br.com.gerencimentodepedidos.data.dto.request.OrderItemRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderItemResponseDTO;
import br.com.gerencimentodepedidos.service.OrderItemService;
import br.com.gerencimentodepedidos.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/items")
@Tag(name = "Order Item", description = "EndPoints for the OrderItem class")
public class OrderItemController implements OrderItemControllerDocs {
    @Autowired
    OrderItemService services;
    @Autowired
    OrderService serviceOrder;

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public OrderItemResponseDTO create(@Valid OrderItemRequestDTO item){
        OrderItemResponseDTO itemCreated = services.createOrderItem(item);
        serviceOrder.fullValue(item.getOrderId());
        return itemCreated;
    }

    @GetMapping(value = "/{itemId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public OrderItemResponseDTO findById(Long id){
        return services.findOrderItemById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<OrderItemResponseDTO> findAll(){
        return services.findAllOrderItems();
    }

    @PutMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public OrderItemResponseDTO update(Long id, @Valid OrderItemRequestDTO item){
        return services.updateOrderItemById(id, item);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(Long id){
        services.deleteOrderItemById(id);
        return ResponseEntity.noContent().build();
    }

}
