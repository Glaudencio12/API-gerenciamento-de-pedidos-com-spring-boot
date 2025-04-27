package br.com.gerencimentodepedidos.controller;

import br.com.gerencimentodepedidos.model.OrderEntity;
import br.com.gerencimentodepedidos.model.OrderItemEntity;
import br.com.gerencimentodepedidos.service.OrderItemService;
import br.com.gerencimentodepedidos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderItemController {
    @Autowired
    OrderItemService services;
    @Autowired
    OrderService seriveOrder;

    @PostMapping(value = "/{orderId}/items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderItemEntity create(@PathVariable("orderId") Long orderId, @RequestBody OrderItemEntity item){
        OrderItemEntity itemCreated = services.createItemOrder(orderId, item);
        seriveOrder.fullValue(orderId);
        return itemCreated;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderItemEntity findById(@PathVariable("id") Long id){
        return services.findById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderItemEntity> findAll(){
        return services.findAll();
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrderItemEntity update(@RequestBody OrderItemEntity item){
        return services.updateItem(item);
    }

    @DeleteMapping("/delete/item/{id_item}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        services.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

}
