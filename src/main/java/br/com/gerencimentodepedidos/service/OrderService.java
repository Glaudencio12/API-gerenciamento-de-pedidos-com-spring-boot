package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.request.OrderRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderResponseDTO;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.mapper.ObjectMapper;
import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.model.OrderItem;
import br.com.gerencimentodepedidos.repository.OrderRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final HateoasLinks hateoas;

    public OrderService(OrderRepository repositoryOrder, HateoasLinks hateoas) {
        this.repository = repositoryOrder;
        this.hateoas = hateoas;
    }

    private final Logger logger = LoggerFactory.getLogger(OrderService.class.getName());

    public OrderResponseDTO createOrder(OrderRequestDTO order) {
        logger.info("Creating a order!");
        var entity = ObjectMapper.parseObject(order, Order.class);
        var dto = ObjectMapper.parseObject(repository.save(entity), OrderResponseDTO.class);
        hateoas.links(dto);
        return dto;
    }

    public OrderResponseDTO findOrderById(Long id) {
        logger.info("Find a order!");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found for this id"));
        var dto = ObjectMapper.parseObject(entity, OrderResponseDTO.class);
        dto.getItems().forEach(orderItemDTO -> {
            hateoas.links(orderItemDTO);
            hateoas.links(orderItemDTO.getProduct());
        });
        hateoas.links(dto);
        return dto;
    }

    public List<OrderResponseDTO> findAllOrders() {
        logger.info("Finding all orders!");
        var dtos = ObjectMapper.parseListObjects(repository.findAll(), OrderResponseDTO.class);
        dtos.forEach(orderDTO -> {
            orderDTO.getItems().forEach(orderItemDTO -> {
                hateoas.links(orderItemDTO);
                hateoas.links(orderItemDTO.getProduct());
            });
            hateoas.links(orderDTO);
        });
        return dtos;
    }

    public void deleteOrderById(Long id) {
        Order order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found for this id"));
        repository.delete(order);
    }

    public void fullValue(Long id) {
        Order order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found for this id"));
        double valueItem = 0;
        double valueFinalOrder = 0;
        for (OrderItem item : order.getItems()) {
            int quantity = item.getQuantity();
            double price = item.getProduct().getPrice();
            valueItem = price * quantity;
            valueFinalOrder += valueItem;
        }

        order.setFullValue(valueFinalOrder);
        repository.save(order);
    }

    public void updateTotalOrderValue() {
        List<Order> pedidos = repository.findAll();
        for (Order pedido : pedidos) {
            fullValue(pedido.getId());
        }
    }

}
