package br.com.gerencimentodepedidos.controller;

import br.com.gerencimentodepedidos.controller.docs.OrderItemControllerDocs;
import br.com.gerencimentodepedidos.data.dto.request.OrderItemRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderItemResponseDTO;
import br.com.gerencimentodepedidos.service.OrderItemService;
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
@RequestMapping("api/v1/items")
@Tag(name = "Order Item", description = "EndPoints for the OrderItem class")
public class OrderItemController implements OrderItemControllerDocs {

    private final OrderItemService services;
    private final OrderService serviceOrder;

    public OrderItemController(OrderItemService services, OrderService serviceOrder) {
        this.services = services;
        this.serviceOrder = serviceOrder;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<OrderItemResponseDTO> create(@Valid OrderItemRequestDTO item) {
        OrderItemResponseDTO itemCreated = services.createOrderItem(item);
        serviceOrder.fullValue(item.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(itemCreated);
    }

    @GetMapping(value = "/{itemId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<OrderItemResponseDTO> findById(Long id) {
        return ResponseEntity.ok(services.findOrderItemById(id));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<OrderItemResponseDTO>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(services.findAllOrderItemsPage(pageable));
    }

    @PutMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<OrderItemResponseDTO> update(Long id, @Valid OrderItemRequestDTO item) {
        return ResponseEntity.ok(services.updateOrderItemById(id, item));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(Long id) {
        services.deleteOrderItemById(id);
        return ResponseEntity.noContent().build();
    }
}
