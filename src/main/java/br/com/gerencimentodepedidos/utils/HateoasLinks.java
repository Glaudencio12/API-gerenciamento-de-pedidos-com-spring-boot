package br.com.gerencimentodepedidos.utils;

import br.com.gerencimentodepedidos.controller.OrderController;
import br.com.gerencimentodepedidos.controller.OrderItemController;
import br.com.gerencimentodepedidos.controller.ProductController;
import br.com.gerencimentodepedidos.data.dto.response.OrderItemResponseDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderResponseDTO;
import br.com.gerencimentodepedidos.data.dto.response.ProductResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class HateoasLinks {
    
    public void links(Object dto) {

        if (dto instanceof ProductResponseDTO product) {
            addProductLinks(product);

        } else if (dto instanceof OrderResponseDTO order) {
            addOrderLinks(order);

        } else if (dto instanceof OrderItemResponseDTO item) {
            addOrderItemLinks(item);
        }
    }
    
    private void addProductLinks(ProductResponseDTO dto) {
        dto.add(linkTo(methodOn(ProductController.class)
                .findById(dto.getId()))
                .withRel("findProductById")
                .withType("GET")
                .withTitle("Search for product"));

        dto.add(linkTo(methodOn(ProductController.class)
                .findAll(null))
                .withRel("findAllProducts")
                .withType("GET")
                .withTitle("Search for all products"));

        dto.add(linkTo(methodOn(ProductController.class)
                .create(null))
                .withRel("createProduct")
                .withType("POST")
                .withTitle("Create product"));

        dto.add(linkTo(methodOn(ProductController.class)
                .update(dto.getId(), null))
                .withRel("updateProductById")
                .withType("PUT")
                .withTitle("Update product"));

        dto.add(linkTo(methodOn(ProductController.class)
                .delete(dto.getId()))
                .withRel("deleteProductById")
                .withType("DELETE")
                .withTitle("Delete product"));
    }
    
    private void addOrderLinks(OrderResponseDTO dto) {
        dto.add(linkTo(methodOn(OrderController.class)
                .findById(dto.getId()))
                .withRel("findOrderById")
                .withType("GET")
                .withTitle("Search for order"));

        dto.add(linkTo(methodOn(OrderController.class)
                .findAll(null))
                .withRel("findAllOrders")
                .withType("GET")
                .withTitle("Search for all orders"));

        dto.add(linkTo(methodOn(OrderController.class)
                .create(null))
                .withRel("createOrder")
                .withType("POST")
                .withTitle("Create order"));

        dto.add(linkTo(methodOn(OrderController.class)
                .delete(dto.getId()))
                .withRel("deleteOrderById")
                .withType("DELETE")
                .withTitle("Delete order"));
    }
    
    private void addOrderItemLinks(OrderItemResponseDTO dto) {
        dto.add(linkTo(methodOn(OrderItemController.class)
                .findById(dto.getId()))
                .withRel("findOrderItemById")
                .withType("GET")
                .withTitle("Search for item"));

        dto.add(linkTo(methodOn(OrderItemController.class)
                .findAll(null))
                .withRel("findAllOrderItems")
                .withType("GET")
                .withTitle("Search for all items"));

        dto.add(linkTo(methodOn(OrderItemController.class)
                .create(null))
                .withRel("createOrderItem")
                .withType("POST")
                .withTitle("Create item"));

        dto.add(linkTo(methodOn(OrderItemController.class)
                .update(dto.getId(), null))
                .withRel("updateOrderItemById")
                .withType("PUT")
                .withTitle("Update item"));

        dto.add(linkTo(methodOn(OrderItemController.class)
                .delete(dto.getId()))
                .withRel("deleteOrderItemById")
                .withType("DELETE")
                .withTitle("Delete item"));
    }
}
