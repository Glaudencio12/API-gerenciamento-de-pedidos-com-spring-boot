package br.com.gerencimentodepedidos.utils;

import br.com.gerencimentodepedidos.controller.OrderController;
import br.com.gerencimentodepedidos.controller.OrderItemController;
import br.com.gerencimentodepedidos.controller.ProductController;
import br.com.gerencimentodepedidos.data.dto.OrderDTO;
import br.com.gerencimentodepedidos.data.dto.OrderItemDTO;
import br.com.gerencimentodepedidos.data.dto.ProductDTO;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class HateoasLinks {
    public void links(Object dto) {
        if (dto instanceof ProductDTO dto1) {
             dto1.add(linkTo(methodOn(ProductController.class).findById(dto1.getId())).withRel("findById").withType("GET").withTitle("Search for product"));
             dto1.add(linkTo(methodOn(ProductController.class).findAll()).withRel("findAll").withType("GET").withTitle("Search for all products"));
             dto1.add(linkTo(methodOn(ProductController.class).create(dto1)).withRel("create").withType("POST").withTitle("Create product"));
             dto1.add(linkTo(methodOn(ProductController.class).update(dto1)).withRel("update").withType("PUT").withTitle("Update product"));
             dto1.add(linkTo(methodOn(ProductController.class).delete(dto1.getId())).withRel("delete").withType("DELETE").withTitle("Delete product"));
        } else if (dto instanceof OrderDTO dto2) {
            dto2.add(linkTo(methodOn(OrderController.class).findById(dto2.getId())).withRel("findById").withType("GET").withTitle("Search for order"));
            dto2.add(linkTo(methodOn(OrderController.class).findAll()).withRel("findAll").withType("GET").withTitle("Search for all orders"));
            dto2.add(linkTo(methodOn(OrderController.class).create(dto2)).withRel("create").withType("POST").withTitle("Create order"));
            dto2.add(linkTo(methodOn(OrderController.class).delete(dto2.getId())).withRel("delete").withType("DELETE").withTitle("Delete order"));
        }else{
            OrderItemDTO dto3 = (OrderItemDTO) dto;
            dto3.add(linkTo(methodOn(OrderItemController.class).findById(dto3.getId())).withRel("findById").withType("GET").withTitle("Search for item"));
            dto3.add(linkTo(methodOn(OrderItemController.class).findAll()).withRel("findAll").withType("GET").withTitle("Search for item"));
            dto3.add(linkTo(methodOn(OrderItemController.class).update(dto3)).withRel("update").withType("UPDATE").withTitle("Update item"));
            dto3.add(linkTo(methodOn(OrderItemController.class).delete(dto3.getId())).withRel("delete").withType("DELETE").withTitle("Delete item"));
        }
    }

    public void links(Object dto, Long id){
        if (dto instanceof OrderItemDTO dtoItem) {
            dtoItem.add(linkTo(methodOn(OrderItemController.class).create(id, dtoItem)).withRel("create").withType("POST").withTitle("Create item"));
        }
    }
}
