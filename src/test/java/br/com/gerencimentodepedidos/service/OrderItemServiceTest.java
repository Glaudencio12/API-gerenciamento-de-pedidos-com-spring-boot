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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderItemServiceTest {

    private MockItem inputItem;
    private MockOrder inputOrder;
    private MockProduct inputProduct;

    @Mock
    OrderItemRepository itemRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    HateoasLinks hateoas;

    @InjectMocks
    OrderItemService service;

    @BeforeEach
    void setUp() {
        inputProduct = new MockProduct();
        inputItem = new MockItem(inputProduct);
        inputOrder = new MockOrder(inputItem);
    }

    public void assertLink(OrderItemDTO dto, String rel, String href, String type) {
        assertTrue(dto.getLinks().stream().anyMatch(link ->
                link.getRel().value().equals(rel)
                        && link.getHref().endsWith(href)
                        && link.getType().equals(type)));
    }

    @Test
    void createItemOrder() {
        Product product = inputProduct.mockProductEntity(1);
        ProductDTO productDTO = inputProduct.mockProductDTO(1);

        Order order = inputOrder.mockOrder(1);
        order.setId(1L);
        OrderDTO orderDTO = inputOrder.mockOrderDTO(1);
        orderDTO.setId(1L);

        List<OrderItem> itemList = inputItem.mockItemsList(order);
        List<OrderItemDTO> itemDTOList = inputItem.mockItemsDTOList(orderDTO);

        order.setItems(itemList);
        orderDTO.setItems(itemDTOList);

        OrderItem item = inputItem.mockItemEntity(1, order, product, 3);
        OrderItemDTO itemDTO = inputItem.mockItemDTO(1, orderDTO, productDTO, 3);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(itemRepository.save(any(OrderItem.class))).thenReturn(item);

        doCallRealMethod().when(hateoas).links(any(OrderItemDTO.class), any(Long.class));

        var result = service.createItemOrder(orderDTO.getId(), itemDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLink(result, "findById", "/items/1", "GET");
        assertLink(result, "findAll", "/items", "GET");
        assertLink(result, "create", "items/order/1", "POST");
        assertLink(result, "update", "/items/update", "PUT");
        assertLink(result, "delete", "/items/delete/1", "DELETE");
    }

    @Test void findById() {
        Product product = inputProduct.mockProductEntity(1);
        ProductDTO productDTO = inputProduct.mockProductDTO(1);

        Order order = inputOrder.mockOrder(1);
        OrderDTO orderDTO = inputOrder.mockOrderDTO(1);
        order.setId(1L);
        orderDTO.setId(1L);

        OrderItem item = inputItem.mockItemEntity(1, order, product, 3);
        OrderItemDTO itemDTO = inputItem.mockItemDTO(1, orderDTO, productDTO, 3);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        doCallRealMethod().when(hateoas).links(any(OrderItemDTO.class));
        doCallRealMethod().when(hateoas).links(any(OrderItemDTO.class), anyLong());


        var result = service.findById(item.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLink(result, "findById", "/items/1", "GET");
        assertLink(result, "findAll", "/items", "GET");
        assertLink(result, "update", "/items/update", "PUT");
        assertLink(result, "delete", "/items/delete/1", "DELETE");
    }
    @Test void findAll() {}
    @Test void updateItem() {}
    @Test void deleteItem() {}
}
