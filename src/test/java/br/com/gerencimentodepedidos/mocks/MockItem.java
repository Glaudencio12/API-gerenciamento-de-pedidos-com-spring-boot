package br.com.gerencimentodepedidos.mocks;

import br.com.gerencimentodepedidos.data.dto.OrderDTO;
import br.com.gerencimentodepedidos.data.dto.OrderItemDTO;
import br.com.gerencimentodepedidos.data.dto.ProductDTO;
import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.model.OrderItem;
import br.com.gerencimentodepedidos.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MockItem {
    MockOrder orderMock = new MockOrder();
    MockProduct mockProduct = new MockProduct();

    public List<OrderItem> mockItemsList(){
        List<OrderItem> items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Product product = mockProduct.mockProductEntity(i);
            OrderItem item = mockItemEntity(i, null, product, i + 1);
            items.add(item);
        }

        Order order = orderMock.mockOrderEntity(1, items);
        for (OrderItem item : items) {
            item.setOrder(order);
        }

        return items;
    }

    public List<OrderItemDTO> mockItemsDTOList(){
        List<OrderItemDTO> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ProductDTO product = mockProduct.mockProductDTO(i);
            OrderItemDTO item = mockItemDTO(i, null, product, i + 1);
            items.add(item);
        }

        OrderDTO order = orderMock.mockOrderDTO(1, items);
        for (OrderItemDTO item : items) {
            item.setOrderDTO(order);
        }

        return items;
    }

    public OrderItem mockItemEntity(Integer number, Order order, Product product, int quantity){
        OrderItem item = new OrderItem();
        item.setId(Long.valueOf(number));
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        return item;
    }

    public OrderItemDTO mockItemDTO(Integer number, OrderDTO orderDTO, ProductDTO productDTO, int quantity){
        OrderItemDTO item = new OrderItemDTO();
        item.setId(Long.valueOf(number));
        item.setOrderDTO(orderDTO);
        item.setProduct(productDTO);
        item.setQuantity(quantity);
        return item;
    }
}
