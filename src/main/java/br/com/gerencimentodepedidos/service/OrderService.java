package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.OrderDTO;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.mapper.ObjectMapper;
import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.model.OrderItem;
import br.com.gerencimentodepedidos.repository.OrderItemRepository;
import br.com.gerencimentodepedidos.repository.OrderRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository repositoryOrder;

    @Autowired
    OrderItemRepository repositoryItem;

    @Autowired
    HateoasLinks hateoas;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class.getName());

    public OrderDTO createOrder(OrderDTO order){
        logger.info("Creating a order!");
        var entity = ObjectMapper.parseObject(order, Order.class);
        var dto = ObjectMapper.parseObject(repositoryOrder.save(entity), OrderDTO.class);
        hateoas.links(dto);
        return dto;
    }

    public OrderDTO findById(Long id){
        logger.info("Find a order!");
        var entity = repositoryOrder.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order not found for this id"));
        var dto = ObjectMapper.parseObject(entity, OrderDTO.class);
        dto.getItems().forEach(orderItemDTO -> {
            hateoas.links(orderItemDTO);
            hateoas.links(orderItemDTO.getProduct());
        });
        hateoas.links(dto);
        return dto;
    }

    public List<OrderDTO> findAll(){
        logger.info("Finding all orders!");
        var dtos = ObjectMapper.parseListObjects(repositoryOrder.findAll(), OrderDTO.class);
        dtos.forEach(orderDTO -> {
            orderDTO.getItems().forEach(orderItemDTO -> {
                hateoas.links(orderItemDTO);
                hateoas.links(orderItemDTO.getProduct());
            });
            hateoas.links(orderDTO);
        });
        return dtos;
    }

    public void deleteOrder(Long id){
        Order order = repositoryOrder.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order not found for this id"));
        repositoryOrder.delete(order);
    }

    public void fullValue(Long id) {
        Order order = repositoryOrder.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found for this id"));
        double valueItem = 0;
        double valueFinalOrder = 0;
        for (OrderItem item : order.getItems()) {
            int quantity = item.getQuantity();
            double price = item.getProduct().getPrice();
            valueItem = price * quantity;
            valueFinalOrder += valueItem;
        }

        order.setFullValue(valueFinalOrder);
        repositoryOrder.save(order);
    }

    public void updateOrders() {
        List<Order> pedidos = repositoryOrder.findAll();
        for (Order pedido : pedidos) {
            fullValue(pedido.getId());
        }
    }

}
