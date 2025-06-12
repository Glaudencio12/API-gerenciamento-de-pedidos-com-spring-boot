package br.com.gerencimentodepedidos.mocks;

import br.com.gerencimentodepedidos.data.dto.OrderDTO;
import br.com.gerencimentodepedidos.data.dto.OrderItemDTO;
import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class MockOrder {

    MockItem item = new MockItem();

    public List<Order> mockOrderList(){
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orders.add(mockOrderEntity(i, item.mockItemsList()));
        }
        return orders;
    }

    public List<OrderDTO> mockOrderDTOList(){
        List<OrderDTO> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            orders.add(mockOrderDTO(i, item.mockItemsDTOList()));
        }
        return orders;
    }

    public Order mockOrderEntity(Integer number, List<OrderItem> items){
        Order order = new Order();
        order.setId(Long.valueOf(number));
        order.setItems(items);
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        order.setFullValue(total);
        return order;
    }

    public OrderDTO mockOrderDTO(Integer number, List<OrderItemDTO> items){
        OrderDTO order = new OrderDTO();
        order.setId(Long.valueOf(number));
        order.setItems(items);
        double total = 0.0;
        for (OrderItemDTO item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        order.setFullValue(total);
        return order;
    }
}
