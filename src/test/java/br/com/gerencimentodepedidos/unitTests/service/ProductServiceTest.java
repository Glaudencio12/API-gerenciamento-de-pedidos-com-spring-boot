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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    ModelMapper modelMapper;
    @Mock
    OrderService orderService;
    @Mock
    PagedResourcesAssembler<ProductResponseDTO> assembler;

    @InjectMocks
    ProductService service;

    MockProduct mock;
    Product product;
    ProductRequestDTO productRequestDTO;
    ProductResponseDTO productResponseDTO;
    List<Product> products;
    List<ProductRequestDTO> productRequestDTOS;

    @BeforeEach
    void setUp() {
        mock = new MockProduct();

        product = mock.mockProductEntity(1);
        productRequestDTO = mock.mockProductDTORequest(1);
        productResponseDTO = mock.mockProductDTOResponse(1);
        products = mock.mockListProducts();
        productRequestDTOS = mock.mockListProductsDTO();
    }

    @Test
    @DisplayName("Should create a product and add proper HATEOAS links")
    void createProduct() {
        when(modelMapper.map(any(ProductRequestDTO.class), eq(Product.class))).thenReturn(product);
        when(repository.save(ArgumentMatchers.any(Product.class))).thenReturn(product);
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(productResponseDTO);

        doNothing().when(hateoasLinks).links(any(ProductResponseDTO.class));

        var result = service.createProduct(productRequestDTO);

        assertNotNull(result);
        assertNotNull(result.getLinks());

        verify(repository, atLeastOnce()).save(any(Product.class));
        verify(hateoasLinks, atLeastOnce()).links(any(ProductResponseDTO.class));
        verify(modelMapper, times(1)).map(any(Product.class), eq(ProductResponseDTO.class));
        verify(modelMapper, times(1)).map(any(ProductRequestDTO.class), eq(Product.class));
    }

    @Test
    @DisplayName("Should retrieve a product by ID with correct HATEOAS links")
    void findProductById() {
        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(productResponseDTO);

        doNothing().when(hateoasLinks).links(any(ProductResponseDTO.class));

        var result = service.findProductById(1L);

        assertNotNull(result);
        assertNotNull(result.getLinks());

        verify(repository, atLeastOnce()).findById(anyLong());
        verify(hateoasLinks, atLeastOnce()).links(any(ProductResponseDTO.class));
        verify(modelMapper, times(1)).map(any(Product.class), eq(ProductResponseDTO.class));
    }

    @Test
    @DisplayName("Should retrieve all paginated products")
    void findAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(products);

        when(repository.findAll(pageable)).thenReturn(productPage);
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(productResponseDTO);

        doNothing().when(hateoasLinks).links(any(ProductResponseDTO.class));

        List<ProductResponseDTO> productResponseDTOS = productPage.stream().map(
                product -> modelMapper.map(product, ProductResponseDTO.class)
        ).toList();

        PagedModel<EntityModel<ProductResponseDTO>> pagedModel =
                PagedModel.of(
                        productResponseDTOS.stream().map(EntityModel::of).toList(),
                        new PagedModel.PageMetadata(10, 0, products.size())
                );

        when(assembler.toModel(any(Page.class))).thenReturn(pagedModel);

        PagedModel<EntityModel<ProductResponseDTO>> respostaPagedModel = service.findAllProductsPage(pageable);

        assertNotNull(respostaPagedModel);
        assertEquals(4, respostaPagedModel.getContent().size());
        verify(repository).findAll(pageable);
        verify(modelMapper, times(8)).map(any(Product.class), eq(ProductResponseDTO.class));
        verify(assembler).toModel(any(Page.class));
    }

    @Test
    @DisplayName("Should update a product by ID and return updated HATEOAS links")
    void updateProductById() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(productResponseDTO);

        doNothing().when(hateoasLinks).links(any(ProductResponseDTO.class));

        var result = service.updateProductById(1L, productRequestDTO);

        assertNotNull(result);
        assertNotNull(result.getLinks());

        verify(repository, atLeastOnce()).findById(anyLong());
        verify(repository, atLeastOnce()).save(any(Product.class));
        verify(hateoasLinks, atLeastOnce()).links(any(ProductResponseDTO.class));
        verify(modelMapper, times(1)).map(any(Product.class), eq(ProductResponseDTO.class));
    }

    @Test
    @DisplayName("Should update a product by ID and return updated HATEOAS links")
    void updateProductField() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Produto Novo");
        fields.put("price", 25.0);

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenReturn(product);
        when(modelMapper.map(any(Product.class), eq(ProductResponseDTO.class))).thenReturn(productResponseDTO);

        ProductResponseDTO result = service.updateProductField(fields, 1L);

        assertNotNull(result);
        assertEquals("Produto Novo", product.getName());
        assertEquals(25.0, product.getPrice());

        assertNotNull(result);
        assertNotNull(result.getLinks());

        verify(repository, atLeastOnce()).findById(anyLong());
        verify(repository, atLeastOnce()).save(any(Product.class));
        verify(hateoasLinks, atLeastOnce()).links(any(ProductResponseDTO.class));
        verify(modelMapper, times(1)).map(any(Product.class), eq(ProductResponseDTO.class));
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

    @Nested
    @DisplayName("When product does not exist")
    class WhenProductDoesNotExist {

        @BeforeEach
        void setupNotFound() {
            when(repository.findById(0L)).thenReturn(Optional.empty());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when finding product by ID")
        void shouldThrowExceptionWhenFindingProduct() {

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> service.findProductById(0L)
            );
            assertEquals("Product not found for this id", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when updating product by ID")
        void shouldThrowExceptionWhenUpdatingProduct() {

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> service.updateProductById(0L, productRequestDTO)
            );
            assertEquals("Product not found for this id", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when deleting product by ID")
        void shouldThrowExceptionWhenDeletingProduct() {

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> service.deleteProductById(0L)
            );
            assertEquals("Product not found for this id", exception.getMessage());
        }
    }
}