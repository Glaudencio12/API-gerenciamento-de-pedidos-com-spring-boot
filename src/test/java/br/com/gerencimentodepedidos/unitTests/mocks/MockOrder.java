package br.com.gerencimentodepedidos.unitTests.mocks;

import br.com.gerencimentodepedidos.data.dto.request.OrderItemRequestDTO;
import br.com.gerencimentodepedidos.data.dto.request.OrderRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderItemResponseDTO;
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
        if (number == null) {
            throw new IllegalArgumentException("Number cannot be null");
        }
        
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

    public OrderRequestDTO mockOrderDTO(Integer number){
        if (number == null) {
            throw new IllegalArgumentException("Number cannot be null");
        }
        
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setId(Long.valueOf(number));

        Order order = new Order();
        order.setId(Long.valueOf(number));
        List<OrderItemRequestDTO> items = mockItem.mockItemsDTOListRequest();
        orderRequestDTO.setItems(items);

        List<OrderItemResponseDTO> itemsResponse = mockItem.mockItemsDTOListResponse();
        double total = 0.0;
        for (OrderItemResponseDTO item : itemsResponse) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        orderRequestDTO.setFullValue(total);
        return orderRequestDTO;
    }

    public List<Order> mockOrderList() {
        List<Order> orders = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
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

    public List<OrderRequestDTO> mockOrderDTOList() {
        List<OrderRequestDTO> orders = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            OrderRequestDTO order = new OrderRequestDTO();
            order.setId((long) i);

            Order orderEntity = new Order();
            orderEntity.setId((long) i);
            List<OrderItemRequestDTO> items = mockItem.mockItemsDTOListRequest();
            order.setItems(items);

            List<OrderItemResponseDTO> itemsResponse = mockItem.mockItemsDTOListResponse();
            double total = 0.0;
            for (OrderItemResponseDTO item : itemsResponse) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
            order.setFullValue(total);

            orders.add(order);
        }
        return orders;
    }
}
