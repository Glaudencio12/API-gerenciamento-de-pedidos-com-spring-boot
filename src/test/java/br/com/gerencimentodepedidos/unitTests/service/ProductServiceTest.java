package br.com.gerencimentodepedidos.unitTests.service;

import br.com.gerencimentodepedidos.data.dto.request.ProductRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.ProductResponseDTO;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.service.OrderService;
import br.com.gerencimentodepedidos.service.ProductService;
import br.com.gerencimentodepedidos.unitTests.mocks.MockProduct;
import br.com.gerencimentodepedidos.model.Product;
import br.com.gerencimentodepedidos.repository.ProductRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.junit.jupiter.api.*;
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
    ProductRequestDTO productRequestDTO;
    List<Product> products;
    List<ProductRequestDTO> productRequestDTOS;

    @BeforeEach
    void setUp() {
        mock = new MockProduct();

        product = mock.mockProductEntity(1);
        productRequestDTO = mock.mockProductDTORequest(1);
        products = mock.mockListProducts();
        productRequestDTOS = mock.mockListProductsDTO();
    }

    public static void assertLinks(ProductResponseDTO dto, String rel, String href, String type) {
        assertTrue(dto.getLinks().stream().anyMatch(link ->
                link.getRel().value().equals(rel) &&
                link.getHref().endsWith(href) &&
                link.getType().equals(type))
        );
    }

    @Test
    @DisplayName("Should create a product and add proper HATEOAS links")
    void createProduct() {
        when(repository.save(ArgumentMatchers.any(Product.class))).thenReturn(product);
        lenient().doCallRealMethod().when(hateoasLinks).links(any(ProductResponseDTO.class));

        var result = service.createProduct(productRequestDTO);

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
    @DisplayName("Should retrieve a product by ID with correct HATEOAS links")
    void findProductById() {
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        lenient().doCallRealMethod().when(hateoasLinks).links(any(ProductResponseDTO.class));

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
    @DisplayName("Should retrieve all products with correct HATEOAS links")
    void findAllProducts() {
        when(repository.findAll()).thenReturn(products);
        lenient().doCallRealMethod().when(hateoasLinks).links(any(ProductResponseDTO.class));

        List<ProductResponseDTO> result = service.findAllProducts();

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
    @DisplayName("Should update a product by ID and return updated HATEOAS links")
    void updateProductById() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
        lenient().doCallRealMethod().when(hateoasLinks).links(any(ProductResponseDTO.class));

        var result = service.updateProductById(1L, productRequestDTO);

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
    @DisplayName("Should delete a product by ID successfully")
    void deleteProductById() {
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        doNothing().when(repository).delete(product);

        service.deleteProductById(product.getId());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Product.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product is not found")
    void checksTheExceptionLaunch(){
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
           service.findProductById(0L);
           service.updateProductById(0L, productRequestDTO);
           service.deleteProductById(0L);
        });

        assertEquals("Product not found for this id", exception.getMessage());
    }
}