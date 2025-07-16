package br.com.gerencimentodepedidos.controller;

import br.com.gerencimentodepedidos.controller.docs.OrderItemControllerDocs;
import br.com.gerencimentodepedidos.data.dto.OrderItemDTO;
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

    @PostMapping(value = "/order/{orderId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public OrderItemDTO create(@PathVariable("orderId") Long orderId, @RequestBody OrderItemDTO item){
        OrderItemDTO itemCreated = services.createOrderItem(orderId, item);
        serviceOrder.fullValue(orderId);
        return itemCreated;
    }

    @GetMapping(value = "/{itemId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public OrderItemDTO findById(@PathVariable("itemId") Long id){
        return services.findOrderItemById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<OrderItemDTO> findAll(){
        return services.findAllOrderItems();
    }

    @PutMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public OrderItemDTO update(@PathVariable("id") Long id, @RequestBody @Valid OrderItemDTO item){
        return services.updateOrderItemById(id, item);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        services.deleteOrderItemById(id);
        return ResponseEntity.noContent().build();
    }

}
