package br.com.gerencimentodepedidos.controller;

import br.com.gerencimentodepedidos.data.dto.OrderItemDTO;
import br.com.gerencimentodepedidos.service.OrderItemService;
import br.com.gerencimentodepedidos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class OrderItemController {
    @Autowired
    OrderItemService services;
    @Autowired
    OrderService serviceOrder;

    @PostMapping(value = "/order/{orderId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE },
                 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE }
    )
    public OrderItemDTO create(@PathVariable("orderId") Long orderId, @RequestBody OrderItemDTO item){
        OrderItemDTO itemCreated = services.createItemOrder(orderId, item);
        serviceOrder.fullValue(orderId);
        return itemCreated;
    }

    @GetMapping(value = "/{itemId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE })
    public OrderItemDTO findById(@PathVariable("itemId") Long id){
        return services.findById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE })
    public List<OrderItemDTO> findAll(){
        return services.findAll();
    }

    @PutMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE },
                consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE }
    )
    public OrderItemDTO update(@RequestBody OrderItemDTO item){
        return services.updateItem(item);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        services.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

}
