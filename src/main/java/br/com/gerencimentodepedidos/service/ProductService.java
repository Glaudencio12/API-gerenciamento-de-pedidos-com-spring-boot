package br.com.gerencimentodepedidos.service;

import br.com.gerencimentodepedidos.exception.ResourceNotFoundException;
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

    public ProductEntity create(ProductEntity product){
        logger.info("Creating a Product!");
        return repository.save(product);
    }

    public ProductEntity findById(Long id){
        logger.info("Finding a product");
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
    }

    public List<ProductEntity> findAll(){
        logger.info("Finding all products");
        return repository.findAll();
    }

    public ProductEntity updateProduct(ProductEntity product){
        logger.info("Updating a Product!");
        ProductEntity entity = repository.findById(product.getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found for this id"));
        entity.setName(product.getName());
        entity.setCategory(product.getCategory());
        entity.setPrice(product.getPrice());
        return repository.save(product);
    }

    public void deleteProduct(Long id){
        logger.info("Deleting a product!");
        ProductEntity product = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found for this id"));
        repository.delete(product);
    }

}
