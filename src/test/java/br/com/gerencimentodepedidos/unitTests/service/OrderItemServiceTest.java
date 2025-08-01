package br.com.gerencimentodepedidos.unitTests.service;

import br.com.gerencimentodepedidos.data.dto.request.OrderRequestDTO;
import br.com.gerencimentodepedidos.data.dto.request.OrderItemRequestDTO;
import br.com.gerencimentodepedidos.data.dto.request.ProductRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderItemResponseDTO;
import br.com.gerencimentodepedidos.data.dto.response.ProductResponseDTO;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.service.OrderItemService;
import br.com.gerencimentodepedidos.service.OrderService;
import br.com.gerencimentodepedidos.unitTests.mocks.MockItem;
import br.com.gerencimentodepedidos.unitTests.mocks.MockOrder;
import br.com.gerencimentodepedidos.unitTests.mocks.MockProduct;
import br.com.gerencimentodepedidos.model.Order;
import br.com.gerencimentodepedidos.model.OrderItem;
import br.com.gerencimentodepedidos.model.Product;
import br.com.gerencimentodepedidos.repository.OrderItemRepository;
import br.com.gerencimentodepedidos.repository.OrderRepository;
import br.com.gerencimentodepedidos.repository.ProductRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.junit.jupiter.api.*;
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
    OrderRequestDTO orderRequestDTO;
    Product product;
    ProductRequestDTO productRequestDTO;
    OrderItem orderItem;
    List<OrderItem> orderItems;
    OrderItemRequestDTO orderItemRequestDTO;

    @BeforeEach
    void setUp() {
        mockProduct = new MockProduct();
        mockItem = new MockItem(mockProduct);
        mockOrder = new MockOrder(mockItem);

        order = mockOrder.mockOrder(1);
        orderRequestDTO = mockOrder.mockOrderDTO(1);
        product = mockProduct.mockProductEntity(1);
        productRequestDTO = mockProduct.mockProductDTORequest(1);
        orderItem = mockItem.mockItemEntity(1, order, product, 2);
        orderItems = mockItem.mockItemsList(order);
        orderItemRequestDTO = mockItem.mockItemDTORequest(1, order.getId(), product.getId(), 2);
    }

    public static void assertLinks(OrderItemResponseDTO dto, String rel, String href, String type) {
        assertTrue(dto.getLinks().stream().anyMatch(link ->
                link.getRel().value().equals(rel) &&
                link.getHref().endsWith(href) &&
                link.getType().equals(type))
        );
    }

    @Test
    @DisplayName("Should create an order item and add correct HATEOAS links")
    void createOrderItem() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(repository.save(any(OrderItem.class))).thenReturn(orderItem);

        doCallRealMethod().when(hateoasLinks).links(any(OrderItemResponseDTO.class));
        doCallRealMethod().when(hateoasLinks).links(any(ProductResponseDTO.class));

        var result = service.createOrderItem(orderItemRequestDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "createOrderItem","api/v1/items", "POST");
        assertLinks(result, "findOrderItemById","api/v1/items/1", "GET");
        assertLinks(result, "findAllOrderItems", "api/v1/items", "GET");
        assertLinks(result, "updateOrderItemById", "api/v1/items/1", "PUT");
        assertLinks(result, "deleteOrderItemById", "api/v1/items/1", "DELETE");

        verify(hateoasLinks, times(1)).links(any(ProductResponseDTO.class));
    }

    @Test
    @DisplayName("Should retrieve an order item by ID with correct HATEOAS links")
    void findOrderItemById() {
        when(repository.findById(orderItem.getId())).thenReturn(Optional.of(orderItem));
        lenient().doCallRealMethod().when(hateoasLinks).links(any(OrderItemResponseDTO.class));
        lenient().doCallRealMethod().when(hateoasLinks).links(any(ProductResponseDTO.class));

        var result = service.findOrderItemById(orderItemRequestDTO.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "createOrderItem","api/v1/items", "POST");
        assertLinks(result, "findOrderItemById","api/v1/items/1", "GET");
        assertLinks(result, "findAllOrderItems", "api/v1/items", "GET");
        assertLinks(result, "updateOrderItemById", "api/v1/items/1", "PUT");
        assertLinks(result, "deleteOrderItemById", "api/v1/items/1", "DELETE");

        verify(hateoasLinks, times(1)).links(any(ProductResponseDTO.class));
    }

    @Test
    @DisplayName("Should retrieve all order items with correct HATEOAS links for each item")
    void findAllOrderItems() {
        when(repository.findAll()).thenReturn(orderItems);
        doCallRealMethod().when(hateoasLinks).links(any(OrderItemResponseDTO.class));
        doCallRealMethod().when(hateoasLinks).links(any(ProductResponseDTO.class));

        List<OrderItemResponseDTO> result = service.findAllOrderItems();

        assertNotNull(result);
        assertEquals(4, result.size());

        var orderItemOne = result.get(1);
        assertLinks(orderItemOne, "createOrderItem","api/v1/items", "POST");
        assertLinks(orderItemOne, "findOrderItemById","api/v1/items/2", "GET");
        assertLinks(orderItemOne, "findAllOrderItems", "api/v1/items", "GET");
        assertLinks(orderItemOne, "updateOrderItemById", "api/v1/items/2", "PUT");
        assertLinks(orderItemOne, "deleteOrderItemById", "api/v1/items/2", "DELETE");

        var orderItemTwo = result.get(3);
        assertLinks(orderItemTwo, "createOrderItem","api/v1/items", "POST");
        assertLinks(orderItemTwo, "findOrderItemById","api/v1/items/4", "GET");
        assertLinks(orderItemTwo, "findAllOrderItems", "api/v1/items", "GET");
        assertLinks(orderItemTwo, "updateOrderItemById", "api/v1/items/4", "PUT");
        assertLinks(orderItemTwo, "deleteOrderItemById", "api/v1/items/4", "DELETE");

        verify(hateoasLinks, times(4)).links(any(ProductResponseDTO.class));
    }

    @Test
    @DisplayName("Should update an order item by ID and return updated HATEOAS links")
    void updateOrderItemById() {
        when(repository.findById(orderItem.getId())).thenReturn(Optional.of(orderItem));
        when(productRepository.findById(orderItemRequestDTO.getProductId())).thenReturn(Optional.of(product));
        when(repository.save(orderItem)).thenReturn(orderItem);

        doCallRealMethod().when(hateoasLinks).links(any(ProductResponseDTO.class));
        doCallRealMethod().when(hateoasLinks).links(any(OrderItemResponseDTO.class));

        var result = service.updateOrderItemById(orderItemRequestDTO.getId(), orderItemRequestDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "createOrderItem","api/v1/items", "POST");
        assertLinks(result, "findOrderItemById","api/v1/items/1", "GET");
        assertLinks(result, "findAllOrderItems", "api/v1/items", "GET");
        assertLinks(result, "updateOrderItemById", "api/v1/items/1", "PUT");
        assertLinks(result, "deleteOrderItemById", "api/v1/items/1", "DELETE");

        verify(hateoasLinks, times(1)).links(any(ProductResponseDTO.class));
    }

    @Test
    @DisplayName("Should delete an order item by ID successfully")
    void deleteOrderItemById() {
        when(repository.findById(orderItem.getId())).thenReturn(Optional.of(orderItem));
        doNothing().when(repository).delete(orderItem);

        service.deleteOrderItemById(orderItem.getId());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(OrderItem.class));
    }

    @Nested
    @DisplayName("Exception Handling for OrderItem Operations")
    class ExceptionOrderItem{

        @Test
        @DisplayName("Should throw ResourceNotFoundException when not finding, updating or deleting a  item")
        void checksTheExceptionLaunch1(){
            Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
                service.findOrderItemById(0L);
                service.updateOrderItemById(0L, null);
                service.deleteOrderItemById(0L);
            });

            assertEquals("Item not found for this id", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when product is not found")
        void shouldThrowWhenProductNotFound() {
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                service.createOrderItem(orderItemRequestDTO);
            });

            assertEquals("Product not found for this id", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when order is not found")
        void shouldThrowWhenOrderNotFound() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(orderRepository.findById(1L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                service.createOrderItem(orderItemRequestDTO);
            });

            assertEquals("Order not found for this id", exception.getMessage());
        }
    }
}