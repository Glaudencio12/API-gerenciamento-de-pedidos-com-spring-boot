package br.com.gerencimentodepedidos.utils;

import br.com.gerencimentodepedidos.controller.OrderController;
import br.com.gerencimentodepedidos.controller.OrderItemController;
import br.com.gerencimentodepedidos.controller.ProductController;
import br.com.gerencimentodepedidos.data.dto.response.OrderItemResponseDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderResponseDTO;
import br.com.gerencimentodepedidos.data.dto.response.ProductResponseDTO;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class HateoasLinks {
    public void links(Object dto) {
        if (dto instanceof ProductResponseDTO dto1) {
            dto1.add(linkTo(methodOn(ProductController.class).findById(dto1.getId())).withRel("findProductById").withType("GET").withTitle("Search for product"));
            dto1.add(linkTo(methodOn(ProductController.class).findAll()).withRel("findAllProducts").withType("GET").withTitle("Search for all products"));
            dto1.add(linkTo(methodOn(ProductController.class).create(null)).withRel("createProduct").withType("POST").withTitle("Create product"));
            dto1.add(linkTo(methodOn(ProductController.class).update(dto1.getId(), null)).withRel("updateProductById").withType("PUT").withTitle("Update product"));
            dto1.add(linkTo(methodOn(ProductController.class).delete(dto1.getId())).withRel("deleteProductById").withType("DELETE").withTitle("Delete product"));

        } else if (dto instanceof OrderResponseDTO dto2) {
            dto2.add(linkTo(methodOn(OrderController.class).findById(dto2.getId())).withRel("findOrderById").withType("GET").withTitle("Search for order"));
            dto2.add(linkTo(methodOn(OrderController.class).findAll()).withRel("findAllOrders").withType("GET").withTitle("Search for all orders"));
            dto2.add(linkTo(methodOn(OrderController.class).create(null)).withRel("createOrder").withType("POST").withTitle("Create order"));
            dto2.add(linkTo(methodOn(OrderController.class).delete(dto2.getId())).withRel("deleteOrderById").withType("DELETE").withTitle("Delete order"));

        } else if (dto instanceof OrderItemResponseDTO dtoItem) {
            dtoItem.add(linkTo(methodOn(OrderItemController.class).findById(dtoItem.getId())).withRel("findOrderItemById").withType("GET").withTitle("Search for item"));
            dtoItem.add(linkTo(methodOn(OrderItemController.class).findAll()).withRel("findAllOrderItems").withType("GET").withTitle("Search for all items"));
            dtoItem.add(linkTo(methodOn(OrderItemController.class).create(null)).withRel("createOrderItem").withType("POST").withTitle("Create item"));
            dtoItem.add(linkTo(methodOn(OrderItemController.class).update(dtoItem.getId(), null)).withRel("updateOrderItemById").withType("PUT").withTitle("Update item"));
            dtoItem.add(linkTo(methodOn(OrderItemController.class).delete(dtoItem.getId())).withRel("deleteOrderItemById").withType("DELETE").withTitle("Delete item"));
        }
    }
}
