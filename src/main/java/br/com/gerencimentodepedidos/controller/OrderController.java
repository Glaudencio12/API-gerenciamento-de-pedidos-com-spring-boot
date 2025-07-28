package br.com.gerencimentodepedidos.controller;

import br.com.gerencimentodepedidos.controller.docs.OrderControllerDocs;
import br.com.gerencimentodepedidos.data.dto.request.OrderRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderResponseDTO;
import br.com.gerencimentodepedidos.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@Tag(name = "Order", description = "EndPoints para for the Order class")
public class OrderController implements OrderControllerDocs {
    @Autowired
    OrderService services;

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public OrderResponseDTO create(@RequestBody @Valid OrderRequestDTO order){
        return services.createOrder(order);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public OrderResponseDTO findById(@PathVariable("id") Long id){
        return services.findOrderById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<OrderResponseDTO> findAll(){
        return services.findAllOrders();
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        services.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
