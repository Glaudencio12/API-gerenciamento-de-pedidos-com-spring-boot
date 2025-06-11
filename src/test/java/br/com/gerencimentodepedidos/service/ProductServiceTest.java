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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest {
    @Mock
    ProductRepository repository;
    
    @Mock
    HateoasLinks hateoas;

    @InjectMocks
    ProductService service;

    MockProduct input;

    @BeforeEach
    void setUp() {
        input = new MockProduct();
        MockitoAnnotations.openMocks(this);
    }

    public void assertLink(ProductDTO dto, String rel, String href, String type){
        assertTrue(dto.getLinks().stream().anyMatch(link -> link.getRel().value().equals(rel)
                && link.getHref().endsWith(href)
                && link.getType().equals(type)));
    }

    @Test
    void create() {
        Product product = input.mockProductEntity(1);
        ProductDTO productDTO = input.mockProductDTO(1);

        when(repository.save(any(Product.class))).thenReturn(product);
        doCallRealMethod().when(hateoas).links(any(ProductDTO.class));


        var result = service.create(productDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

       assertLink(result, "findById", "/products/1", "GET" );
       assertLink(result, "findAll", "/products", "GET" );
       assertLink(result, "create", "/products", "POST" );
       assertLink(result, "update", "/products/update", "PUT" );
       assertLink(result, "delete", "/products/1", "DELETE" );
    }

    @Test
    void findById() {
        Product product = input.mockProductEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        doCallRealMethod().when(hateoas).links(any(ProductDTO.class));

        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

       assertLink(result, "findById", "/products/1", "GET" );
       assertLink(result, "findAll", "/products", "GET" );
       assertLink(result, "create", "/products", "POST" );
       assertLink(result, "update", "/products/update", "PUT" );
       assertLink(result, "delete", "/products/1", "DELETE" );
    }

    @Test
    void findAll() {
        List<Product> products = input.mockListProducts();
        when(repository.findAll()).thenReturn(products);
        doCallRealMethod().when(hateoas).links(any(ProductDTO.class));
        List<ProductDTO> productDTOS = service.findAll();

        assertNotNull(productDTOS);
        assertEquals(10, productDTOS.size());

        var productOne = productDTOS.get(1);

       assertLink(productOne, "findById", "/products/1", "GET" );
       assertLink(productOne, "findAll", "/products", "GET" );
       assertLink(productOne, "create", "/products", "POST" );
       assertLink(productOne, "update", "/products/update", "PUT" );
       assertLink(productOne, "delete", "/products/1", "DELETE" );
    }

    @Test
    void updateProduct() {
        Product product = input.mockProductEntity(1);
        ProductDTO productDTO = input.mockProductDTO(1);
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(product));
        when(repository.save(any(Product.class))).thenReturn(product);

        var result = service.updateProduct(productDTO);

       assertLink(result, "findById", "/products/1", "GET" );
       assertLink(result, "findAll", "/products", "GET" );
       assertLink(result, "create", "/products", "POST" );
       assertLink(result, "update", "/products/update", "PUT" );
       assertLink(result, "delete", "/products/1", "DELETE" );
    }

    @Test
    void deleteProduct() {
        Product product = input.mockProductEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        service.deleteProduct(1L);

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Product.class));
    }
}