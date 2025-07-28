package br.com.gerencimentodepedidos.unitTests.mocks;

import br.com.gerencimentodepedidos.data.dto.request.OrderItemRequestDTO;
import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.model.OrderItem;
import br.com.gerencimentodepedidos.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MockItem {

    private final MockProduct mockProduct;

    public MockItem(MockProduct mockProduct) {
        this.mockProduct = mockProduct;
    }

    public List<OrderItem> mockItemsList(Order order) {
        List<OrderItem> items = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Product product = mockProduct.mockProductEntity(i);
            OrderItem item = mockItemEntity(i, order, product, i + 1);
            items.add(item);
        }
        return items;
    }

    public List<OrderItemRequestDTO> mockItemsDTOList(Order order) {
        List<OrderItemRequestDTO> items = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Product product = mockProduct.mockProductEntity(i);
            OrderItemRequestDTO item = mockItemDTO(i, order, product, i + 1);
            items.add(item);
        }
        return items;
    }

    public OrderItem mockItemEntity(Integer number, Order order, Product product, int quantity) {
        if (number == null || quantity <= 0) {
            throw new IllegalArgumentException("Number and quantity must be valid");
        }
        
        OrderItem item = new OrderItem();
        item.setId(Long.valueOf(number));
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        return item;
    }

    public OrderItemRequestDTO mockItemDTO(Integer number, Order order, Product product, int quantity) {
        if (number == null || quantity <= 0) {
            throw new IllegalArgumentException("Number and quantity must be valid");
        }
        
        OrderItemRequestDTO item = new OrderItemRequestDTO();
        item.setId(Long.valueOf(number));
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        return item;
    }
}
