package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.OrderDTO;
import br.com.gerencimentodepedidos.data.dto.OrderItemDTO;
import br.com.gerencimentodepedidos.data.dto.ProductDTO;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.mapper.ObjectMapper;
import br.com.gerencimentodepedidos.model.OrderEntity;
import br.com.gerencimentodepedidos.model.OrderItemEntity;
import br.com.gerencimentodepedidos.model.ProductEntity;
import br.com.gerencimentodepedidos.repository.OrderItemRepository;
import br.com.gerencimentodepedidos.repository.OrderRepository;
import br.com.gerencimentodepedidos.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    OrderItemRepository repository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;

    Logger logger = LoggerFactory.getLogger(OrderItemService.class.getName());

    public OrderItemDTO createItemOrder(Long orderId, OrderItemDTO item){
        logger.info("Creating a order!");
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Item not found for this id"));
        ProductEntity product = productRepository.findById(item.getProduct().getId()).orElseThrow(()-> new ResourceNotFoundException("Item not found for this id"));
        item.setOrder(ObjectMapper.parseObject(order, OrderDTO.class));
        item.setProduct(ObjectMapper.parseObject(product, ProductDTO.class));
        item.setQuantity(item.getQuantity());
        var entity = ObjectMapper.parseObject(item, OrderItemEntity.class);
        return ObjectMapper.parseObject(repository.save(entity), OrderItemDTO.class);
    }

    public OrderItemDTO findById(Long id){
        logger.info("Find a order!");
        var entity = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Item not found for this id"));
        return ObjectMapper.parseObject(entity, OrderItemDTO.class);
    }

    public List<OrderItemDTO> findAll(){
        logger.info("Finding all orders!");
        return ObjectMapper.parseListObjects(repository.findAll(), OrderItemDTO.class);
    }

    public OrderItemDTO updateItem(OrderItemDTO item){
        OrderItemEntity itemEntity = repository.findById(item.getId()).orElseThrow(()-> new ResourceNotFoundException("Order not found for this id"));
        itemEntity.setProduct(ObjectMapper.parseObject(item.getProduct(), ProductEntity.class));
        itemEntity.setQuantity(item.getQuantity());
        var entity = ObjectMapper.parseObject(itemEntity, OrderItemEntity.class);
        return ObjectMapper.parseObject(repository.save(entity), OrderItemDTO.class);
    }

    public void deleteItem(Long id){
        OrderItemEntity item = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order not found for this id"));
        repository.delete(item);
    }
}
