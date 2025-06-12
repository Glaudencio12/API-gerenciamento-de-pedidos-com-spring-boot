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
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderItemServiceTest {

    private MockItem inputItem;
    private MockOrder inputOrder;
    private MockProduct inputProduct;

    @Mock
    OrderItemRepository repository;

    @Mock
    HateoasLinks hateoas;

    @InjectMocks
    OrderItemService service;

    @BeforeEach
    void setUp() {
        inputItem = new MockItem();
        inputOrder = new MockOrder();
        inputProduct = new MockProduct();
        MockitoAnnotations.openMocks(this);
    }

    public void assertLink(OrderItemDTO dto, String rel, String href, String type){
        assertTrue(dto.getLinks().stream().anyMatch(link -> link.getRel().value().equals(rel)
                && link.getHref().endsWith(href)
                && link.getType().equals(type)));
    }

    @Test
    void createItemOrder() {
        Product product = inputProduct.mockProductEntity(1);
        ProductDTO productDTO = inputProduct.mockProductDTO(1);

        Order order = inputOrder.mockOrderEntity(1, inputItem.mockItemsList());
        OrderDTO orderDTO = inputOrder.mockOrderDTO(1, inputItem.mockItemsDTOList());

        OrderItem item = inputItem.mockItemEntity(1, order, product, 3);
        OrderItemDTO itemDTO = inputItem.mockItemDTO(1, orderDTO, productDTO, 3);

        when(repository.save(any(OrderItem.class))).thenReturn(item);
        doCallRealMethod().when(hateoas).links(itemDTO);

        var result = service.createItemOrder(order.getId(), itemDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLink(result, "findById", "/items/1", "GET" );
        assertLink(result, "findAll", "/items", "GET" );
        assertLink(result, "create", "items/order/1", "POST" );
        assertLink(result, "update", "/items/update", "PUT" );
        assertLink(result, "delete", "/items/delete/1", "DELETE" );
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void deleteItem() {
    }
}