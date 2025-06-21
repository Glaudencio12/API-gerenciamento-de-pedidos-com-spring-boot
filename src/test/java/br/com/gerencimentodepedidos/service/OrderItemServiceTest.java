package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.OrderDTO;
import br.com.gerencimentodepedidos.data.dto.OrderItemDTO;
import br.com.gerencimentodepedidos.data.dto.ProductDTO;
import br.com.gerencimentodepedidos.mocks.MockItem;
import br.com.gerencimentodepedidos.mocks.MockOrder;
import br.com.gerencimentodepedidos.mocks.MockProduct;
import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.model.OrderItem;
import br.com.gerencimentodepedidos.model.Product;
import br.com.gerencimentodepedidos.repository.OrderItemRepository;
import br.com.gerencimentodepedidos.repository.OrderRepository;
import br.com.gerencimentodepedidos.repository.ProductRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @Mock
    OrderItemRepository repository;
    @Mock
    ProductRepository productRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderService orderService;
    @Mock
    HateoasLinks hateoasLinks;

    @InjectMocks
    OrderItemService service;

    MockItem mockItem;
    MockOrder mockOrder;
    MockProduct mockProduct;

    Order order;
    OrderDTO orderDTO;
    Product product;
    ProductDTO productDTO;
    OrderItem orderItem;
    List<OrderItem> orderItems;
    OrderItemDTO orderItemDTO;

    @BeforeEach
    void setUp() {
        mockProduct = new MockProduct();
        mockItem = new MockItem(mockProduct);
        mockOrder = new MockOrder(mockItem);

        order = mockOrder.mockOrder(1);
        orderDTO = mockOrder.mockOrderDTO(1);
        product = mockProduct.mockProductEntity(1);
        productDTO = mockProduct.mockProductDTO(1);
        orderItem = mockItem.mockItemEntity(1, order, product, 2);
        orderItems = mockItem.mockItemsList(order);
        orderItemDTO = mockItem.mockItemDTO(1, orderDTO, productDTO, 2);
    }

    public static void assertLinks(OrderItemDTO dto, String rel, String href, String type) {
        assertTrue(dto.getLinks().stream().anyMatch(link ->
                link.getRel().value().equals(rel) &&
                link.getHref().endsWith(href) &&
                link.getType().equals(type))
        );
    }

    @Test
    void createOrderItem() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(repository.save(any(OrderItem.class))).thenReturn(orderItem);

        doCallRealMethod().when(hateoasLinks).links(any(OrderItemDTO.class));
        doCallRealMethod().when(hateoasLinks).links(any(ProductDTO.class));

        var result = service.createOrderItem(orderItemDTO.getId(), orderItemDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "createOrderItem","api/v1/items/order/1", "POST");
        assertLinks(result, "findOrderItemById","api/v1/items/1", "GET");
        assertLinks(result, "findAllOrderItems", "api/v1/items", "GET");
        assertLinks(result, "updateOrderItemById", "api/v1/items/1", "PUT");
        assertLinks(result, "deleteOrderItemById", "api/v1/items/1", "DELETE");

        verify(hateoasLinks, times(1)).links(any(ProductDTO.class));
    }

    @Test
    void findOrderItemById() {
        when(repository.findById(orderItem.getId())).thenReturn(Optional.of(orderItem));
        doCallRealMethod().when(hateoasLinks).links(any(OrderItemDTO.class));
        doCallRealMethod().when(hateoasLinks).links(any(ProductDTO.class));

        var result = service.findOrderItemById(orderItemDTO.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "createOrderItem","api/v1/items/order/1", "POST");
        assertLinks(result, "findOrderItemById","api/v1/items/1", "GET");
        assertLinks(result, "findAllOrderItems", "api/v1/items", "GET");
        assertLinks(result, "updateOrderItemById", "api/v1/items/1", "PUT");
        assertLinks(result, "deleteOrderItemById", "api/v1/items/1", "DELETE");

        verify(hateoasLinks, times(1)).links(any(ProductDTO.class));
    }

    @Test
    void findAllOrderItems() {
        when(repository.findAll()).thenReturn(orderItems);
        doCallRealMethod().when(hateoasLinks).links(any(OrderItemDTO.class));
        doCallRealMethod().when(hateoasLinks).links(any(ProductDTO.class));

        List<OrderItemDTO> result = service.findAllOrderItems();

        assertNotNull(result);
        assertEquals(4, result.size());

        var orderItemOne = result.get(1);
        assertLinks(orderItemOne, "createOrderItem","api/v1/items/order/1", "POST");
        assertLinks(orderItemOne, "findOrderItemById","api/v1/items/2", "GET");
        assertLinks(orderItemOne, "findAllOrderItems", "api/v1/items", "GET");
        assertLinks(orderItemOne, "updateOrderItemById", "api/v1/items/2", "PUT");
        assertLinks(orderItemOne, "deleteOrderItemById", "api/v1/items/2", "DELETE");

        var orderItemTwo = result.get(3);
        assertLinks(orderItemTwo, "createOrderItem","api/v1/items/order/1", "POST");
        assertLinks(orderItemTwo, "findOrderItemById","api/v1/items/4", "GET");
        assertLinks(orderItemTwo, "findAllOrderItems", "api/v1/items", "GET");
        assertLinks(orderItemTwo, "updateOrderItemById", "api/v1/items/4", "PUT");
        assertLinks(orderItemTwo, "deleteOrderItemById", "api/v1/items/4", "DELETE");

        verify(hateoasLinks, times(4)).links(any(ProductDTO.class));
    }

    @Test
    void updateOrderItemById() {
        when(repository.findById(orderItem.getId())).thenReturn(Optional.of(orderItem));
        when(repository.save(orderItem)).thenReturn(orderItem);
        doCallRealMethod().when(hateoasLinks).links(any(OrderItemDTO.class));
        doCallRealMethod().when(hateoasLinks).links(any(ProductDTO.class));

        var result = service.updateOrderItemById(orderItem.getId(), orderItemDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "createOrderItem","api/v1/items/order/1", "POST");
        assertLinks(result, "findOrderItemById","api/v1/items/1", "GET");
        assertLinks(result, "findAllOrderItems", "api/v1/items", "GET");
        assertLinks(result, "updateOrderItemById", "api/v1/items/1", "PUT");
        assertLinks(result, "deleteOrderItemById", "api/v1/items/1", "DELETE");

        verify(hateoasLinks, times(1)).links(any(ProductDTO.class));
    }

    @Test
    void deleteOrderItemById() {
        when(repository.findById(orderItem.getId())).thenReturn(Optional.of(orderItem));
        doNothing().when(repository).delete(orderItem);

        service.deleteOrderItemById(orderItem.getId());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(OrderItem.class));
    }
}