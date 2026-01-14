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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

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
    @Mock
    ModelMapper modelMapper;
    @Mock
    PagedResourcesAssembler<OrderItemResponseDTO> assembler;

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
    OrderItemResponseDTO orderItemResponseDTO;

    @BeforeEach
    void setUp() {
        mockProduct = new MockProduct();
        mockItem = new MockItem(mockProduct);
        mockOrder = new MockOrder(mockItem);

        order = mockOrder.mockOrder(1);
        orderRequestDTO = mockOrder.mockOrderRequestDTO(1);
        product = mockProduct.mockProductEntity(1);
        productRequestDTO = mockProduct.mockProductDTORequest(1);
        orderItem = mockItem.mockItemEntity(1, order, product, 2);
        orderItems = mockItem.mockItemsList(order);
        orderItemRequestDTO = mockItem.mockItemDTORequest(1, order.getId(), product.getId(), 2);
        orderItemResponseDTO = mockItem.mockItemDTOResponse(1, 4);
    }

    @Test
    @DisplayName("Should create an order item and add correct HATEOAS links")
    void createOrderItem() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(repository.save(any(OrderItem.class))).thenReturn(orderItem);

        doNothing().when(hateoasLinks).links(any(OrderItemResponseDTO.class));
        doNothing().when(hateoasLinks).links(any(ProductResponseDTO.class));

        var result = service.createOrderItem(orderItemRequestDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        verify(hateoasLinks, times(1)).links(any(ProductResponseDTO.class));
    }

    @Test
    @DisplayName("Should retrieve an order item by ID with correct HATEOAS links")
    void findOrderItemById() {
        when(repository.findById(orderItem.getId())).thenReturn(Optional.of(orderItem));
        when(modelMapper.map(any(OrderItem.class), eq(OrderItemResponseDTO.class))).thenReturn(orderItemResponseDTO);
        doNothing().when(hateoasLinks).links(any(ProductResponseDTO.class));
        doNothing().when(hateoasLinks).links(any(OrderItemResponseDTO.class));

        var result = service.findOrderItemById(orderItem.getId());

        assertNotNull(result);
        assertNotNull(result.getProduct());
        assertNotNull(result.getLinks());

        verify(repository, times(1)).findById(orderItem.getId());
        verify(modelMapper, times(1)).map(any(OrderItem.class), eq(OrderItemResponseDTO.class));
        verify(hateoasLinks, times(1)).links(any(ProductResponseDTO.class));
        verify(hateoasLinks, times(1)).links(any(OrderItemResponseDTO.class));
    }


    @Test
    @DisplayName("Should retrieve all order items with correct HATEOAS links for each item")
    void findAllOrderItems() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderItem> orderItemPage = new PageImpl<>(orderItems);

        when(repository.findAll(pageable)).thenReturn(orderItemPage);
        when(modelMapper.map(any(OrderItem.class), eq(OrderItemResponseDTO.class))).thenReturn(orderItemResponseDTO);

        doNothing().when(hateoasLinks).links(any(OrderItemResponseDTO.class));
        doNothing().when(hateoasLinks).links(any(ProductResponseDTO.class));

        List<OrderItemResponseDTO> orderItemResponseDTOS = orderItemPage.stream().map(
                orderItem -> modelMapper.map(orderItem, OrderItemResponseDTO.class)
        ).toList();

        PagedModel<EntityModel<OrderItemResponseDTO>> pagedModel =
                PagedModel.of(
                        orderItemResponseDTOS.stream().map(EntityModel::of).toList(),
                        new PagedModel.PageMetadata(10, 0, orderItems.size())
                );

        when(assembler.toModel(any(Page.class))).thenReturn(pagedModel);

        PagedModel<EntityModel<OrderItemResponseDTO>> respostaPagedModel = service.findAllOrderItemsPage(pageable);

        assertNotNull(respostaPagedModel);
        assertEquals(4, respostaPagedModel.getContent().size());
        verify(repository).findAll(pageable);
        verify(modelMapper, times(8)).map(any(OrderItem.class), eq(OrderItemResponseDTO.class));
        verify(assembler).toModel(any(Page.class));

        verify(hateoasLinks, times(4)).links(any(OrderItemResponseDTO.class));
        verify(hateoasLinks, times(4)).links(any(ProductResponseDTO.class));
    }

    @Test
    @DisplayName("Should update an order item by ID and return updated HATEOAS links")
    void updateOrderItemById() {
        when(repository.findById(orderItem.getId())).thenReturn(Optional.of(orderItem));
        when(productRepository.findById(orderItemRequestDTO.getProductId())).thenReturn(Optional.of(product));
        when(modelMapper.map(any(OrderItem.class), eq(OrderItemResponseDTO.class))).thenReturn(orderItemResponseDTO);
        when(repository.save(orderItem)).thenReturn(orderItem);

        doNothing().when(hateoasLinks).links(any(ProductResponseDTO.class));
        doNothing().when(hateoasLinks).links(any(OrderItemResponseDTO.class));

        var result = service.updateOrderItemById(orderItemRequestDTO.getId(), orderItemRequestDTO);

        assertNotNull(result);
        assertNotNull(result.getProduct());
        assertNotNull(result.getLinks());

        verify(repository, times(1)).findById(orderItem.getId());
        verify(modelMapper, times(1)).map(any(OrderItem.class), eq(OrderItemResponseDTO.class));
        verify(hateoasLinks, times(1)).links(any(ProductResponseDTO.class));
        verify(hateoasLinks, times(1)).links(any(OrderItemResponseDTO.class));
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
    class ExceptionOrderItem {

        @Test
        @DisplayName("Should throw ResourceNotFoundException when OrderItem is not found on find")
        void shouldThrowWhenOrderItemNotFoundOnFind() {
            when(repository.findById(0L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> service.findOrderItemById(0L)
            );

            assertEquals("Item not found for this id", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when OrderItem is not found on update")
        void shouldThrowWhenOrderItemNotFoundOnUpdate() {
            when(repository.findById(0L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> service.updateOrderItemById(0L, null)
            );

            assertEquals("Item not found for this id", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when OrderItem is not found on delete")
        void shouldThrowWhenOrderItemNotFoundOnDelete() {
            when(repository.findById(0L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> service.deleteOrderItemById(0L)
            );

            assertEquals("Item not found for this id", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when product is not found")
        void shouldThrowWhenProductNotFound() {
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> service.createOrderItem(orderItemRequestDTO)
            );

            assertEquals("Product not found for this id", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when order is not found")
        void shouldThrowWhenOrderNotFound() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(orderRepository.findById(1L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> service.createOrderItem(orderItemRequestDTO)
            );

            assertEquals("Order not found for this id", exception.getMessage());
        }
    }

}