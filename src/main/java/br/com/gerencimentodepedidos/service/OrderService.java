package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.model.OrderEntity;
import br.com.gerencimentodepedidos.model.OrderItemEntity;
import br.com.gerencimentodepedidos.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository repository;
    private final Logger logger = LoggerFactory.getLogger(OrderService.class.getName());

    public OrderEntity createOrder(OrderEntity order){
        logger.info("Creating a order!");
        return repository.save(order);
    }

    public OrderEntity findById(Long id){
        logger.info("Find a order!");
        return repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order not found for this id"));
    }

    public List<OrderEntity> findAll(){
        logger.info("Finding all orders!");
        return repository.findAll();
    }

    public void deleteOrder(Long id){
        OrderEntity order = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order not found for this id"));
        repository.delete(order);
    }

    public void fullValue(Long id) {
        OrderEntity order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found for this id"));
        double valueItem = 0;
        double valueFinalOrder = 0;
        for (OrderItemEntity item : order.getItems()) {
            int quantity = item.getQuantity();
            double price = item.getProduct().getPrice();
            valueItem = price * quantity;
            valueFinalOrder += valueItem;
        }

        order.setFullValue(valueFinalOrder);
        repository.save(order);
    }

}
