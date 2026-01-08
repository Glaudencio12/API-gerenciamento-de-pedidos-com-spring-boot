package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.request.ProductRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.ProductResponseDTO;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.model.Product;
import br.com.gerencimentodepedidos.repository.ProductRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final OrderService OrderService;
    private final HateoasLinks hateoas;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository repository, OrderService serviceOrder, HateoasLinks hateoas, ModelMapper modelMapper) {
        this.repository = repository;
        this.OrderService = serviceOrder;
        this.hateoas = hateoas;
        this.modelMapper = modelMapper;
    }

    private final Logger logger = LoggerFactory.getLogger(ProductService.class.getName());

    public ProductResponseDTO createProduct(ProductRequestDTO product) {
        logger.info("Creating a Product!");
        product.setName(product.getName().trim());
        var entity = modelMapper.map(product, Product.class);
        var dto = modelMapper.map(repository.save(entity), ProductResponseDTO.class);
        hateoas.links(dto);
        return dto;

    }

    public ProductResponseDTO findProductById(Long id) {
        logger.info("Finding a product");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        var dto = modelMapper.map(entity, ProductResponseDTO.class);
        hateoas.links(dto);
        return dto;
    }
    public List<ProductResponseDTO> findAllProducts() {
        logger.info("Finding all products");
        var products = repository.findAll();
        List<ProductResponseDTO> dtos = new ArrayList<>();
        products.forEach(product -> {
            ProductResponseDTO dto = modelMapper.map(product, ProductResponseDTO.class);
            hateoas.links(dto);
            dtos.add(dto);
        });
        return dtos;
    }


    public ProductResponseDTO updateProductById(Long id, ProductRequestDTO product) {
        logger.info("Updating a Product!");
        Product entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        entity.setName(product.getName().trim());
        entity.setCategory(product.getCategory());
        entity.setPrice(product.getPrice());
        var dto = modelMapper.map(repository.save(entity), ProductResponseDTO.class);
        OrderService.updateTotalOrderValue();
        hateoas.links(dto);
        return dto;
    }

    public ProductResponseDTO updateProductField(Map<String, Object> fields, Long id) {
        Product registeredEntity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));

        Set<String> allowedFields = Set.of("name", "price", "category");

        for (String key : fields.keySet()) {
            if (!allowedFields.contains(key)) {
                throw new IllegalArgumentException("The field provided is not allowed for changes");
            }
        }

        fields.forEach((campo, valor) -> {
            Field field = ReflectionUtils.findField(Product.class, campo);

            if (campo.equals("name") || campo.equals("category")) {
                if (!(valor instanceof String) || ((String) valor).isBlank()) {
                    throw new IllegalArgumentException(field.getName() + " must be a non-empty string");
                }
            } else if (campo.equals("price")) {
                if (!(valor instanceof Number) || ((Number) valor).doubleValue() < 0) {
                    throw new IllegalArgumentException(field.getName() + " must be a non-negative number");
                }
            }

            field.setAccessible(true);
            ReflectionUtils.setField(field, registeredEntity, valor);
        });

        ProductResponseDTO dto = modelMapper.map(repository.save(registeredEntity), ProductResponseDTO.class);
        hateoas.links(dto);
        return dto;
    }


    public void deleteProductById(Long id) {
        logger.info("Deleting a product!");
        Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        repository.delete(product);
    }
}
