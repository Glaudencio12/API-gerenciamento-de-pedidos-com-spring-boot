package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.ProductDTO;
import br.com.gerencimentodepedidos.mocks.MockProduct;
import br.com.gerencimentodepedidos.model.Product;
import br.com.gerencimentodepedidos.repository.ProductRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository repository;
    @Mock
    HateoasLinks hateoasLinks;
    @Mock
    OrderService orderService;

    @InjectMocks
    ProductService service;

    MockProduct mock;
    Product product;
    ProductDTO productDTO;
    List<Product> products;
    List<ProductDTO> productDTOS;

    @BeforeEach
    void setUp() {
        mock = new MockProduct();

        product = mock.mockProductEntity(1);
        productDTO = mock.mockProductDTO(1);
        products = mock.mockListProducts();
        productDTOS = mock.mockListProductsDTO();
    }

    public static void assertLinks(ProductDTO dto, String rel, String href, String type) {
        assertTrue(dto.getLinks().stream().anyMatch(link ->
                link.getRel().value().equals(rel) &&
                link.getHref().endsWith(href) &&
                link.getType().equals(type))
        );
    }

    @Test
    void createProduct() {
        when(repository.save(ArgumentMatchers.any(Product.class))).thenReturn(product);
        doCallRealMethod().when(hateoasLinks).links(any(ProductDTO.class));

        var result = service.createProduct(productDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "findProductById", "/api/v1/products/1", "GET");
        assertLinks(result, "findAllProducts", "/api/v1/products", "GET");
        assertLinks(result, "createProduct", "/api/v1/products", "POST");
        assertLinks(result, "updateProductById", "/api/v1/products/1", "PUT");
        assertLinks(result, "deleteProductById", "/api/v1/products/1", "DELETE");
    }

    @Test
    void findProductById() {
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        doCallRealMethod().when(hateoasLinks).links(productDTO);

        var result = service.findProductById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "findProductById", "/api/v1/products/1", "GET");
        assertLinks(result, "findAllProducts", "/api/v1/products", "GET");
        assertLinks(result, "createProduct", "/api/v1/products", "POST");
        assertLinks(result, "updateProductById", "/api/v1/products/1", "PUT");
        assertLinks(result, "deleteProductById", "/api/v1/products/1", "DELETE");
    }

    @Test
    void findAllProducts() {
        when(repository.findAll()).thenReturn(products);
        productDTOS.forEach(dto -> lenient().doCallRealMethod().when(hateoasLinks).links(dto));

        List<ProductDTO> result = service.findAllProducts();

        assertNotNull(result);
        assertEquals(4, result.size());

        var productOne = result.get(1);

        assertLinks(productOne, "findProductById", "/api/v1/products/2", "GET");
        assertLinks(productOne, "findAllProducts", "/api/v1/products", "GET");
        assertLinks(productOne, "createProduct", "/api/v1/products", "POST");
        assertLinks(productOne, "updateProductById", "/api/v1/products/2", "PUT");
        assertLinks(productOne, "deleteProductById", "/api/v1/products/2", "DELETE");

        var productTwo = result.get(3);

        assertLinks(productTwo, "findProductById", "/api/v1/products/4", "GET");
        assertLinks(productTwo, "findAllProducts", "/api/v1/products", "GET");
        assertLinks(productTwo, "createProduct", "/api/v1/products", "POST");
        assertLinks(productTwo, "updateProductById", "/api/v1/products/4", "PUT");
        assertLinks(productTwo, "deleteProductById", "/api/v1/products/4", "DELETE");
    }

    @Test
    void updateProductById() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
        doCallRealMethod().when(hateoasLinks).links(productDTO);

        var result = service.updateProductById(1L, productDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "findProductById", "/api/v1/products/1", "GET");
        assertLinks(result, "findAllProducts", "/api/v1/products", "GET");
        assertLinks(result, "createProduct", "/api/v1/products", "POST");
        assertLinks(result, "updateProductById", "/api/v1/products/1", "PUT");
        assertLinks(result, "deleteProductById", "/api/v1/products/1", "DELETE");
    }

    @Test
    void deleteProductById() {
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        doNothing().when(repository).delete(product);

        service.deleteProductById(product.getId());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Product.class));
    }
}