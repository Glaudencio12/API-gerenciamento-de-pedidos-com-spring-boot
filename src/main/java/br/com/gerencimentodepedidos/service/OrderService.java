package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.request.OrderRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderResponseDTO;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.model.OrderItem;
import br.com.gerencimentodepedidos.repository.OrderRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final HateoasLinks hateoas;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository repositoryOrder, HateoasLinks hateoas, ModelMapper mapper) {
        this.repository = repositoryOrder;
        this.hateoas = hateoas;
        this.modelMapper = mapper;
    }

    private final Logger logger = LoggerFactory.getLogger(OrderService.class.getName());

    public OrderResponseDTO createOrder(OrderRequestDTO order) {
        logger.info("Creating a order!");
        var entity = modelMapper.map(order, Order.class);
        var dto = modelMapper.map(repository.save(entity), OrderResponseDTO.class);
        hateoas.links(dto);
        return dto;
    }

    public OrderResponseDTO findOrderById(Long id) {
        logger.info("Find a order!");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found for this id"));
        var dto = modelMapper.map(entity, OrderResponseDTO.class);
        dto.getItems().forEach(orderItemDTO -> {
            hateoas.links(orderItemDTO);
            hateoas.links(orderItemDTO.getProduct());
        });
        hateoas.links(dto);
        return dto;
    }

    public List<OrderResponseDTO> findAllOrders() {
        logger.info("Finding all orders!");
        var orders = repository.findAll();
        List<OrderResponseDTO> dtos = new ArrayList<>();
        orders.forEach(order -> {
            OrderResponseDTO orderResponse = modelMapper.map(order, OrderResponseDTO.class);
            orderResponse.getItems().forEach(orderItem -> {
                hateoas.links(orderItem);
                hateoas.links(orderItem.getProduct());
            });
            hateoas.links(orderResponse);
            dtos.add(orderResponse);
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
