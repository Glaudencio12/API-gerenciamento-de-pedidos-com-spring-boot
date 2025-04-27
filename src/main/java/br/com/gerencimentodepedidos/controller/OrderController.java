package br.com.gerencimentodepedidos.controller;

import br.com.gerencimentodepedidos.model.OrderEntity;
import br.com.gerencimentodepedidos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    OrderService services;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderEntity create(@RequestBody OrderEntity order){
        return services.createOrder(order);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderEntity findById(@PathVariable("id") Long id){
        return services.findById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderEntity> findAll(){
        return services.findAll();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        services.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
