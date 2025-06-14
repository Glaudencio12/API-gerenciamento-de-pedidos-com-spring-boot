package br.com.gerencimentodepedidos.mocks;

import br.com.gerencimentodepedidos.data.dto.OrderDTO;
import br.com.gerencimentodepedidos.data.dto.OrderItemDTO;
import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class MockOrder {

    private final MockItem mockItem;

    public MockOrder(MockItem mockItem) {
        this.mockItem = mockItem;
    }

    public Order mockOrder(Integer number){
        Order order = new Order();
        order.setId(Long.valueOf(number));
        List<OrderItem> items = mockItem.mockItemsList(order);
        order.setItems(items);

        double total = 0.0;
        for (OrderItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        order.setFullValue(total);
        return order;
    }

    public OrderDTO mockOrderDTO(Integer number){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(Long.valueOf(number));
        List<OrderItemDTO> items = mockItem.mockItemsDTOList(orderDTO);
        orderDTO.setItems(items);

        double total = 0.0;
        for (OrderItemDTO item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        orderDTO.setFullValue(total);
        return orderDTO;
    }

    public List<Order> mockOrderList() {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Order order = new Order();
            order.setId((long) i);
            List<OrderItem> items = mockItem.mockItemsList(order);
            order.setItems(items);

            double total = 0.0;
            for (OrderItem item : items) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
            order.setFullValue(total);

            orders.add(order);
        }
        return orders;
    }

    public List<OrderDTO> mockOrderDTOList() {
        List<OrderDTO> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderDTO order = new OrderDTO();
            order.setId((long) i);
            List<OrderItemDTO> items = mockItem.mockItemsDTOList(order);
            order.setItems(items);

            double total = 0.0;
            for (OrderItemDTO item : items) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
            order.setFullValue(total);

            orders.add(order);
        }
        return orders;
    }
}
