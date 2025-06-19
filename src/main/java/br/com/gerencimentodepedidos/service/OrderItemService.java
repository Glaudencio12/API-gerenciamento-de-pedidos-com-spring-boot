package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.OrderDTO;
import br.com.gerencimentodepedidos.data.dto.OrderItemDTO;
import br.com.gerencimentodepedidos.data.dto.ProductDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    public OrderItemService(OrderItemRepository repository, ProductRepository productRepository, OrderRepository orderRepository, OrderService serviceOrder, HateoasLinks hateoas) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.serviceOrder = serviceOrder;
        this.hateoas = hateoas;
    }

    public OrderItemDTO createItemOrder(Long orderId, OrderItemDTO item){
        logger.info("Creating a order!");
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Item not found for this id"));
        Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(()-> new ResourceNotFoundException("Item not found for this id"));
        item.setOrderDTO(ObjectMapper.parseObject(order, OrderDTO.class));
        item.setProduct(ObjectMapper.parseObject(product, ProductDTO.class));
        item.setQuantity(item.getQuantity());
        var entity = ObjectMapper.parseObject(item, OrderItem.class);
        var dto = ObjectMapper.parseObject(repository.save(entity), OrderItemDTO.class);
        hateoas.links(dto);
        return dto;
    }

    public OrderItemDTO findById(Long id){
        logger.info("Find a order!");
        var entity = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Item not found for this id"));
        var dto = ObjectMapper.parseObject(entity, OrderItemDTO.class);
        hateoas.links(dto.getProduct());
        hateoas.links(dto);
        return dto;
    }

    public List<OrderItemDTO> findAll(){
        logger.info("Finding all orders!");
        var dtos = ObjectMapper.parseListObjects(repository.findAll(), OrderItemDTO.class);
        dtos.forEach(orderItemDTO -> {
            hateoas.links(orderItemDTO.getProduct());
            hateoas.links(orderItemDTO);
        });
        return dtos;
    }

    public OrderItemDTO updateItem(OrderItemDTO item){
        OrderItem itemEntity = repository.findById(item.getId()).orElseThrow(()-> new ResourceNotFoundException("Order not found for this id"));
        itemEntity.setProduct(ObjectMapper.parseObject(item.getProduct(), Product.class));
        itemEntity.setQuantity(item.getQuantity());
        var entity = ObjectMapper.parseObject(itemEntity, OrderItem.class);
        var dto = ObjectMapper.parseObject(repository.save(entity), OrderItemDTO.class);
        hateoas.links(dto);
        return dto;
    }

    public void deleteItem(Long id){
        OrderItem item = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order not found for this id"));
        repository.delete(item);
        serviceOrder.updateTotalOrderValue();
    }
}
