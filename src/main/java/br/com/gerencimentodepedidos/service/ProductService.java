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
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductService.class.getName());

    public ProductDTO create(ProductDTO product){
        logger.info("Creating a Product!");
        var entity = ObjectMapper.parseObject(product, ProductEntity.class);
        return ObjectMapper.parseObject(repository.save(entity), ProductDTO.class);
    }

    public ProductDTO findById(Long id){
        logger.info("Finding a product");
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        return ObjectMapper.parseObject(entity, ProductDTO.class);
    }

    public List<ProductDTO> findAll(){
        logger.info("Finding all products");
        return ObjectMapper.parseListObjects(repository.findAll(), ProductDTO.class);
    }

    public ProductDTO updateProduct(ProductDTO product){
        logger.info("Updating a Product!");
        ProductEntity entity = repository.findById(product.getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        entity.setName(product.getName());
        entity.setCategory(product.getCategory());
        entity.setPrice(product.getPrice());
        return ObjectMapper.parseObject(repository.save(entity), ProductDTO.class);
    }

    public void deleteProduct(Long id){
        logger.info("Deleting a product!");
        ProductEntity product = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found for this id"));
        repository.delete(product);
    }

}
