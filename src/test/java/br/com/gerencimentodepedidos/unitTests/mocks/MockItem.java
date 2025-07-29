package br.com.gerencimentodepedidos.unitTests.mocks;

import br.com.gerencimentodepedidos.data.dto.request.OrderItemRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderItemResponseDTO;
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

    public OrderItemRequestDTO mockItemDTORequest(Integer number, Long orderId, Long productId, int quantity) {
        if (number == null || quantity <= 0) {
            throw new IllegalArgumentException("Number and quantity must be valid");
        }

        OrderItemRequestDTO item = new OrderItemRequestDTO();
        item.setId(Long.valueOf(number));
        item.setOrderId(orderId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        return item;
    }

    public List<OrderItemRequestDTO> mockItemsDTOListRequest() {
        List<OrderItemRequestDTO> items = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            OrderItemRequestDTO item = mockItemDTORequest(i, (long) i, (long) i, i + 1);
            items.add(item);
        }
        return items;
    }

    public OrderItemResponseDTO mockItemDTOResponse(Integer number, int quantity) {
        if (number == null || quantity <= 0) {
            throw new IllegalArgumentException("Number and quantity must be valid");
        }

        OrderItemResponseDTO item = new OrderItemResponseDTO();
        item.setId(Long.valueOf(number));
        item.setProduct(mockProduct.mockProductDTOResponse(number));
        item.setQuantity(quantity);
        return item;
    }

    public List<OrderItemResponseDTO> mockItemsDTOListResponse() {
        List<OrderItemResponseDTO> items = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Product product = mockProduct.mockProductEntity(i);
            OrderItemResponseDTO item = mockItemDTOResponse(i, i +1);
            items.add(item);
        }
        return items;
    }
}
