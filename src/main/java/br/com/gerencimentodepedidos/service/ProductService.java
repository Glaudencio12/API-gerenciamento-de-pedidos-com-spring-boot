package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.request.ProductRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.ProductResponseDTO;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.mapper.ObjectMapper;
import br.com.gerencimentodepedidos.model.Product;
import br.com.gerencimentodepedidos.repository.ProductRepository;
import br.com.gerencimentodepedidos.utils.HateoasLinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final OrderService OrderService;
    private final HateoasLinks hateoas;

    public ProductService(ProductRepository repository, OrderService serviceOrder, HateoasLinks hateoas) {
        this.repository = repository;
        this.OrderService = serviceOrder;
        this.hateoas = hateoas;
    }

    private final Logger logger = LoggerFactory.getLogger(ProductService.class.getName());

    public ProductResponseDTO createProduct(ProductRequestDTO product) {
        logger.info("Creating a Product!");
        product.setName(product.getName().trim());
        var entity = ObjectMapper.parseObject(product, Product.class);
        var dto = ObjectMapper.parseObject(repository.save(entity), ProductResponseDTO.class);
        hateoas.links(dto);
        return dto;

    }

    public ProductResponseDTO findProductById(Long id) {
        logger.info("Finding a product");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        var dto = ObjectMapper.parseObject(entity, ProductResponseDTO.class);
        hateoas.links(dto);
        return dto;
    }

    public List<ProductResponseDTO> findAllProducts() {
        logger.info("Finding all products");
        var dto = ObjectMapper.parseListObjects(repository.findAll(), ProductResponseDTO.class);
        dto.forEach(hateoas::links);
        return dto;
    }

    public ProductResponseDTO updateProductById(Long id, ProductRequestDTO product) {
        logger.info("Updating a Product!");
        Product entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        entity.setName(product.getName().trim());
        entity.setCategory(product.getCategory());
        entity.setPrice(product.getPrice());
        var dto = ObjectMapper.parseObject(repository.save(entity), ProductResponseDTO.class);
        OrderService.updateTotalOrderValue();
        hateoas.links(dto);
        return dto;
    }

public void deleteProductById(Long id) {
    logger.info("Deleting a product!");
    Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
    repository.delete(product);
}
}
