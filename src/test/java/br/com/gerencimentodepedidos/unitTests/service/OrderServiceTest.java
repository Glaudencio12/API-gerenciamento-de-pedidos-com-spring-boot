package br.com.gerencimentodepedidos.unitTests.service;

import br.com.gerencimentodepedidos.data.dto.request.OrderRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderResponseDTO;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.model.OrderItem;
import br.com.gerencimentodepedidos.model.Product;
import br.com.gerencimentodepedidos.service.OrderService;
import br.com.gerencimentodepedidos.unitTests.mocks.MockItem;
import br.com.gerencimentodepedidos.unitTests.mocks.MockOrder;
import br.com.gerencimentodepedidos.unitTests.mocks.MockProduct;
import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.repository.OrderRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    OrderRequestDTO orderRequestDTO;
    List<Order> orders;
    List<OrderRequestDTO> orderRequestDTOS;

    @BeforeEach
    void setUp() {
        mockProduct = new MockProduct();
        mockItem = new MockItem(mockProduct);
        mockOrder = new MockOrder(mockItem);

        order = mockOrder.mockOrder(1);
        orderRequestDTO = mockOrder.mockOrderDTO(1);
        orders = mockOrder.mockOrderList();
        orderRequestDTOS = mockOrder.mockOrderDTOList();
    }

    public static void assertLinks(OrderResponseDTO dto, String rel, String href, String type) {
        assertTrue(dto.getLinks().stream().anyMatch(link ->
                link.getRel().value().equals(rel) &&
                link.getHref().endsWith(href) &&
                link.getType().equals(type))
        );
    }

    @Test
    @DisplayName("Should create an order and add correct HATEOAS links")
    void createOrder() {
        when(repository.save(any(Order.class))).thenReturn(order);
        doCallRealMethod().when(hateoasLinks).links(any(OrderResponseDTO.class));

        var result = service.createOrder(orderRequestDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "findOrderById", "/api/v1/orders/1", "GET");
        assertLinks(result, "findAllOrders", "/api/v1/orders", "GET");
        assertLinks(result, "createOrder", "/api/v1/orders", "POST");
        assertLinks(result, "deleteOrderById", "/api/v1/orders/1", "DELETE");
    }

    @Test
    @DisplayName("Should retrieve an order by ID with HATEOAS links and verify item/product links")
    void findOrderById() {
        when(repository.findById(order.getId())).thenReturn(Optional.of(order));
        doCallRealMethod().when(hateoasLinks).links(any(Product.class));
        doCallRealMethod().when(hateoasLinks).links(any(OrderItem.class));
        doCallRealMethod().when(hateoasLinks).links(any(OrderResponseDTO.class));

        var result = service.findOrderById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "findOrderById", "/api/v1/orders/1", "GET");
        assertLinks(result, "findAllOrders", "/api/v1/orders", "GET");
        assertLinks(result, "createOrder", "/api/v1/orders", "POST");
        assertLinks(result, "deleteOrderById", "/api/v1/orders/1", "DELETE");

        verify(hateoasLinks, times(4)).links(any(OrderItem.class));
        verify(hateoasLinks, times(4)).links(any(Product.class));
    }

    @Test
    @DisplayName("Should retrieve all orders with proper HATEOAS links and verify item/product links")
    void findAllOrders() {
        when(repository.findAll()).thenReturn(orders);
        doCallRealMethod().when(hateoasLinks).links(any(OrderItem.class));
        doCallRealMethod().when(hateoasLinks).links(any(Product.class));
        doCallRealMethod().when(hateoasLinks).links(any(OrderResponseDTO.class));


        List<OrderResponseDTO> result = service.findAllOrders();

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

        verify(hateoasLinks, times(8)).links(any(OrderItem.class));
        verify(hateoasLinks, times(8)).links(any(Product.class));
    }

    @Test
    @DisplayName("Should delete an order by ID successfully")
    void deleteOrderById() {
        when(repository.findById(order.getId())).thenReturn(Optional.of(order));
        doNothing().when(repository).delete(order);

        service.deleteOrderById(order.getId());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when order is not found")
    void checksTheExceptionLaunch(){
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findOrderById(0L);
            service.deleteOrderById(0L);
            service.fullValue(0L);
        });

        assertEquals("Order not found for this id", exception.getMessage());
    }
}