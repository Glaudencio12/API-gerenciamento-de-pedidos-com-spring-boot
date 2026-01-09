package br.com.gerencimentodepedidos.controller;

import br.com.gerencimentodepedidos.controller.docs.OrderControllerDocs;
import br.com.gerencimentodepedidos.data.dto.request.OrderRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderResponseDTO;
import br.com.gerencimentodepedidos.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
@Tag(name = "Order", description = "EndPoints para for the Order class")
public class OrderController implements OrderControllerDocs {

    private final OrderService services;

    public OrderController(OrderService services) {
        this.services = services;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<OrderResponseDTO> create(@Valid OrderRequestDTO order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(services.createOrder(order));
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<OrderResponseDTO> findById(Long id) {
        return ResponseEntity.ok(services.findOrderById(id));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<OrderResponseDTO>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(services.findAllOrderPage(pageable));
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> delete(Long id) {
        services.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
