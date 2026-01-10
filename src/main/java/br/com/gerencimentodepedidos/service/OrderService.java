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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final HateoasLinks hateoas;
    private final ModelMapper modelMapper;
    private final PagedResourcesAssembler<OrderResponseDTO> assembler;

    public OrderService(
            OrderRepository repositoryOrder,
            HateoasLinks hateoas,
            ModelMapper mapper,
            PagedResourcesAssembler<OrderResponseDTO> assembler
    ) {
        this.repository = repositoryOrder;
        this.hateoas = hateoas;
        this.modelMapper = mapper;
        this.assembler = assembler;
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

    public PagedModel<EntityModel<OrderResponseDTO>> findAllOrderPage(Pageable pageable) {
        logger.info("Finding all orders!");
        Page<Order> orders = repository.findAll(pageable);

        Page<OrderResponseDTO> orderResponseDTOS = orders.map(order -> {
            OrderResponseDTO orderResponseDTO = modelMapper.map(order, OrderResponseDTO.class);
            orderResponseDTO.getItems().forEach(orderItem -> {
                hateoas.links(orderItem);
                hateoas.links(orderItem.getProduct());
            });
            hateoas.links(orderResponseDTO);
            return orderResponseDTO;
        });

        return assembler.toModel(orderResponseDTOS);
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
