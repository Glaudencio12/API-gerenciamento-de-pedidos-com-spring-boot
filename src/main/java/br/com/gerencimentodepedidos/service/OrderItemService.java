package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.request.OrderRequestDTO;
import br.com.gerencimentodepedidos.data.dto.request.OrderItemRequestDTO;
import br.com.gerencimentodepedidos.data.dto.request.ProductRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderItemResponseDTO;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.mapper.ObjectMapper;
import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.model.OrderItem;
import br.com.gerencimentodepedidos.model.Product;
import br.com.gerencimentodepedidos.repository.OrderItemRepository;
import br.com.gerencimentodepedidos.repository.OrderRepository;
import br.com.gerencimentodepedidos.repository.ProductRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository repository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderService serviceOrder;
    private final HateoasLinks hateoas;

    Logger logger = LoggerFactory.getLogger(OrderItemService.class.getName());

    public OrderItemService(
            OrderItemRepository repository, ProductRepository productRepository, OrderRepository orderRepository, OrderService serviceOrder, HateoasLinks hateoas
    ) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.serviceOrder = serviceOrder;
        this.hateoas = hateoas;
    }

    public OrderItemResponseDTO createOrderItem(Long orderId, OrderItemRequestDTO item) {
        logger.info("Creating a order Item!");
        Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found for this id"));
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(item.getQuantity());
        var entity = ObjectMapper.parseObject(item, OrderItem.class);
        var dto = ObjectMapper.parseObject(repository.save(entity), OrderItemResponseDTO.class);
        hateoas.links(dto);
        hateoas.links(dto.getProduct());
        return dto;
    }

    public OrderItemResponseDTO findOrderItemById(Long id) {
        logger.info("Find a order item!");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found for this id"));
        var dto = ObjectMapper.parseObject(entity, OrderItemResponseDTO.class);
        hateoas.links(dto.getProduct()); // Corrigir aqui os links hateoas
        hateoas.links(dto);
        return dto;
    }

    public List<OrderItemResponseDTO> findAllOrderItems() {
        logger.info("Finding all orders items!");
        var dtos = ObjectMapper.parseListObjects(repository.findAll(), OrderItemResponseDTO.class);
        dtos.forEach(orderItemDTO -> {
            hateoas.links(orderItemDTO.getProduct());
            hateoas.links(orderItemDTO);
        });
        return dtos;
    }

    public OrderItemResponseDTO updateOrderItemById(Long id, OrderItemRequestDTO item) {
        logger.info("Updating the order item");
        OrderItem itemEntity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found for this id"));
        itemEntity.setProduct(ObjectMapper.parseObject(item.getProduct(), Product.class));
        itemEntity.setQuantity(item.getQuantity());
        var entity = ObjectMapper.parseObject(itemEntity, OrderItem.class);
        var dto = ObjectMapper.parseObject(repository.save(entity), OrderItemResponseDTO.class);
        hateoas.links(dto.getProduct());
        hateoas.links(dto);
        return dto;

    }

    public void deleteOrderItemById(Long id) {
        OrderItem item = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found for this id"));
        repository.delete(item);
        serviceOrder.updateTotalOrderValue();
    }
}
