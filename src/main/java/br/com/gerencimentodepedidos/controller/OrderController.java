package br.com.gerencimentodepedidos.controller;

import br.com.gerencimentodepedidos.controller.docs.OrderControllerDocs;
import br.com.gerencimentodepedidos.data.dto.OrderDTO;
import br.com.gerencimentodepedidos.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order", description = "EndPoints para for the Order class")
public class OrderController implements OrderControllerDocs {
    @Autowired
    OrderService services;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public OrderDTO create(@RequestBody OrderDTO order){
        return services.createOrder(order);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public OrderDTO findById(@PathVariable("id") Long id){
        return services.findById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<OrderDTO> findAll(){
        return services.findAll();
    }

    @DeleteMapping(value = "/deleteOrder/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        services.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
