package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.ProductDTO;
import br.com.gerencimentodepedidos.mocks.MockProduct;
import br.com.gerencimentodepedidos.model.Product;
import br.com.gerencimentodepedidos.repository.ProductRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import io.swagger.v3.oas.annotations.extensions.Extension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @InjectMocks
    ProductService service;

    MockProduct mock;

    @BeforeEach
    void setUp() {
        mock = new MockProduct();
    }

    public static void assertLinks(ProductDTO dto, String rel, String href, String type) {
        assertTrue(dto.getLinks().stream().anyMatch(link ->
                link.getRel().value().equals(rel) &&
                link.getHref().endsWith(href) &&
                link.getType().equals(type))
        );
    }

    @Test
    void create() {
        Product product = mock.mockProductEntity(1);
        ProductDTO productDTO = mock.mockProductDTO(1);

        when(repository.save(ArgumentMatchers.any(Product.class))).thenReturn(product);
        doCallRealMethod().when(hateoasLinks).links(any(ProductDTO.class));

        var result = service.create(productDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "findById", "/products/1", "GET");
        assertLinks(result, "findAll", "/products", "GET");
        assertLinks(result, "create", "/products", "POST");
        assertLinks(result, "update", "/products/update", "PUT");
        assertLinks(result, "delete", "/products/1", "DELETE");
    }

    @Test
    void findById() {
        Product product = mock.mockProductEntity(1);
        ProductDTO productDTO = mock.mockProductDTO(1);

        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        doCallRealMethod().when(hateoasLinks).links(productDTO);

        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "findById", "/products/1", "GET");
        assertLinks(result, "findAll", "/products", "GET");
        assertLinks(result, "create", "/products", "POST");
        assertLinks(result, "update", "/products/update", "PUT");
        assertLinks(result, "delete", "/products/1", "DELETE");
    }

    @Test
    void findAll() {
        List<Product> products = mock.mockListProducts();
        List<ProductDTO> productDTOS = mock.mockListProductsDTO();

        when(repository.findAll()).thenReturn(products);
        productDTOS.forEach(dto -> doCallRealMethod().when(hateoasLinks).links(dto));

        List<ProductDTO> result = service.findAll();

        assertNotNull(result);
        assertEquals(10, result.size());

        var productOne = result.get(1);

        assertLinks(productOne, "findById", "/products/1", "GET");
        assertLinks(productOne, "findAll", "/products", "GET");
        assertLinks(productOne, "create", "/products", "POST");
        assertLinks(productOne, "update", "/products/update", "PUT");
        assertLinks(productOne, "delete", "/products/1", "DELETE");

        var productTwo = result.get(5);

        assertLinks(productTwo, "findById", "/products/5", "GET");
        assertLinks(productTwo, "findAll", "/products", "GET");
        assertLinks(productTwo, "create", "/products", "POST");
        assertLinks(productTwo, "update", "/products/update", "PUT");
        assertLinks(productTwo, "delete", "/products/5", "DELETE");
    }

    @Test
    void updateProduct() {
        Product product = mock.mockProductEntity(1);
        ProductDTO productDTO = mock.mockProductDTO(1);

        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
        doCallRealMethod().when(hateoasLinks).links(productDTO);

        var result = service.updateProduct(productDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertLinks(result, "findById", "/products/1", "GET");
        assertLinks(result, "findAll", "/products", "GET");
        assertLinks(result, "create", "/products", "POST");
        assertLinks(result, "update", "/products/update", "PUT");
        assertLinks(result, "delete", "/products/1", "DELETE");
    }

    @Test
    void deleteProduct() {
        Product product = mock.mockProductEntity(1);

        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        doNothing().when(repository).delete(product);

        service.deleteProduct(product.getId());

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Product.class));
    }
}