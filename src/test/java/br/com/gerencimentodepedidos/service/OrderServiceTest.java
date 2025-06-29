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
import br.com.gerencimentodepedidos.repository.OrderRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
class OrderServiceTest {

    @Mock
    OrderRepository repository;
    @Mock
    HateoasLinks hateoasLinks;

    @InjectMocks
    OrderService service;

    MockOrder mockOrder;
    MockItem mockItem;
    MockProduct mockProduct;

    Order order;
    OrderDTO orderDTO;
    List<Order> orders;
    List<OrderDTO> orderDTOS;

    @BeforeEach
    void setUp() {
        mockProduct = new MockProduct();
        mockItem = new MockItem(mockProduct);
        mockOrder = new MockOrder(mockItem);

        order = mockOrder.mockOrder(1);
        orderDTO = mockOrder.mockOrderDTO(1);
        orders = mockOrder.mockOrderList();
        orderDTOS = mockOrder.mockOrderDTOList();
    }

    public static void assertLinks(OrderDTO dto, String rel, String href, String type) {
        assertTrue(dto.getLinks().stream().anyMatch(link ->
                link.getRel().value().equals(rel) &&
                link.getHref().endsWith(href) &&
                link.getType().equals(type))
        );
    }

    @Test
    void createOrder() {
        when(repository.save(any(Order.class))).thenReturn(order);
        doCallRealMethod().when(hateoasLinks).links(any(OrderDTO.class));

        var result = service.createOrder(orderDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "findOrderById", "/api/v1/orders/1", "GET");
        assertLinks(result, "findAllOrders", "/api/v1/orders", "GET");
        assertLinks(result, "createOrder", "/api/v1/orders", "POST");
        assertLinks(result, "deleteOrderById", "/api/v1/orders/1", "DELETE");
    }

    @Test
    void findOrderById() {
        when(repository.findById(order.getId())).thenReturn(Optional.of(order));
        doCallRealMethod().when(hateoasLinks).links(any(ProductDTO.class));
        doCallRealMethod().when(hateoasLinks).links(any(OrderDTO.class));
        doCallRealMethod().when(hateoasLinks).links(any(OrderItemDTO.class));

        var result = service.findOrderById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "findOrderById", "/api/v1/orders/1", "GET");
        assertLinks(result, "findAllOrders", "/api/v1/orders", "GET");
        assertLinks(result, "createOrder", "/api/v1/orders", "POST");
        assertLinks(result, "deleteOrderById", "/api/v1/orders/1", "DELETE");

        verify(hateoasLinks, times(4)).links(any(OrderItemDTO.class));
        verify(hateoasLinks, times(4)).links(any(ProductDTO.class));
    }

    @Test
    void findAllOrders() {
        when(repository.findAll()).thenReturn(orders);
        doCallRealMethod().when(hateoasLinks).links(any(OrderDTO.class));
        doCallRealMethod().when(hateoasLinks).links(any(ProductDTO.class));
        doCallRealMethod().when(hateoasLinks).links(any(OrderItemDTO.class));

        List<OrderDTO> result = service.findAllOrders();

        assertNotNull(result);
        assertEquals(2, result.size());

        var orderItemOne = result.get(0);
        assertLinks(orderItemOne, "findOrderById", "/api/v1/orders/1", "GET");
        assertLinks(orderItemOne, "findAllOrders", "/api/v1/orders", "GET");
        assertLinks(orderItemOne, "createOrder","/api/v1/orders", "POST");
        assertLinks(orderItemOne, "deleteOrderById", "/api/v1/orders/1", "DELETE");

        var orderItemTwo = result.get(1);
        assertLinks(orderItemTwo, "findOrderById", "/api/v1/orders/2", "GET");
        assertLinks(orderItemTwo, "findAllOrders", "/api/v1/orders", "GET");
        assertLinks(orderItemTwo, "createOrder", "/api/v1/orders", "POST");
        assertLinks(orderItemTwo, "deleteOrderById", "/api/v1/orders/2", "DELETE");

        verify(hateoasLinks, times(8)).links(any(OrderItemDTO.class));
        verify(hateoasLinks, times(8)).links(any(ProductDTO.class));
    }

    @Test
    void deleteOrderById() {
        when(repository.findById(order.getId())).thenReturn(Optional.of(order));
        doNothing().when(repository).delete(order);

        service.deleteOrderById(order.getId());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Order.class));
    }
}