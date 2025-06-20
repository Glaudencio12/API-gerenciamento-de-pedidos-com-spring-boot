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

    public List<OrderItemDTO> mockItemsDTOList(OrderDTO order) {
        List<OrderItemDTO> items = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            ProductDTO product = mockProduct.mockProductDTO(i);
            OrderItemDTO item = mockItemDTO(i, order, product, i + 1);
            items.add(item);
        }
        return items;
    }

    public OrderItem mockItemEntity(Integer number, Order order, Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.setId(Long.valueOf(number));
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        return item;
    }

    public OrderItemDTO mockItemDTO(Integer number, OrderDTO orderDTO, ProductDTO productDTO, int quantity) {
        OrderItemDTO item = new OrderItemDTO();
        item.setId(Long.valueOf(number));
        item.setOrderDTO(orderDTO);
        item.setProduct(productDTO);
        item.setQuantity(quantity);
        return item;
    }
}
