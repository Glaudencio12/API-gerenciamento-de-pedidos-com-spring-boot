package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.data.dto.ProductDTO;
import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
import br.com.gerencimentodepedidos.mapper.ObjectMapper;
import br.com.gerencimentodepedidos.model.ProductEntity;
import br.com.gerencimentodepedidos.repository.ProductRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository repository;

    @Autowired
    HateoasLinks hateoas;

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductService.class.getName());

    public ProductDTO create(ProductDTO product){
        logger.info("Creating a Product!");
        var entity = ObjectMapper.parseObject(product, ProductEntity.class);
        var dto = ObjectMapper.parseObject(repository.save(entity), ProductDTO.class);
        hateoas.links(dto);
        return dto;
    }

    public ProductDTO findById(Long id){
        logger.info("Finding a product");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        var dto = ObjectMapper.parseObject(entity, ProductDTO.class);
        hateoas.links(dto);
        return dto;
    }

    public List<ProductDTO> findAll(){
        logger.info("Finding all products");
        var dto = ObjectMapper.parseListObjects(repository.findAll(), ProductDTO.class);
        dto.forEach(p -> hateoas.links(p));
        return dto;
    }

    public ProductDTO updateProduct(ProductDTO product){
        logger.info("Updating a Product!");
        ProductEntity entity = repository.findById(product.getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        entity.setName(product.getName());
        entity.setCategory(product.getCategory());
        entity.setPrice(product.getPrice());
        var dto = ObjectMapper.parseObject(repository.save(entity), ProductDTO.class);
        hateoas.links(dto);
        return dto;
    }

    public void deleteProduct(Long id){
        logger.info("Deleting a product!");
        ProductEntity product = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found for this id"));
        repository.delete(product);
    }

}
