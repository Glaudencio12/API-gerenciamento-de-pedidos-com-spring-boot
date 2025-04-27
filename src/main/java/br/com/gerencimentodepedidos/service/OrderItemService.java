package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
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

    public OrderItemEntity createItemOrder(Long orderId, OrderItemEntity item){
        logger.info("Creating a order!");
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Item not found for this id"));
        ProductEntity product = productRepository.findById(item.getProduct().getId()).orElseThrow(()-> new ResourceNotFoundException("Item not found for this id"));
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(item.getQuantity());
        return repository.save(item);
    }

    public OrderItemEntity findById(Long id){
        logger.info("Find a order!");
        return repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Item not found for this id"));
    }

    public List<OrderItemEntity> findAll(){
        logger.info("Finding all orders!");
        return repository.findAll();
    }

    public OrderItemEntity updateItem(OrderItemEntity item){
        OrderItemEntity itemEntity = repository.findById(item.getId()).orElseThrow(()-> new ResourceNotFoundException("Order not found for this id"));
        itemEntity.setProduct(item.getProduct());
        itemEntity.setQuantity(item.getQuantity());
        return repository.save(itemEntity);
    }

    public void deleteItem(Long id){
        OrderItemEntity item = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order not found for this id"));
        repository.delete(item);
    }
}
