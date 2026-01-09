package br.com.gerencimentodepedidos.controller;

import br.com.gerencimentodepedidos.controller.docs.ProductControllerDocs;
import br.com.gerencimentodepedidos.data.dto.request.ProductRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.ProductResponseDTO;
import br.com.gerencimentodepedidos.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product", description = "Endpoints for the Product class")
public class ProductController implements ProductControllerDocs {

    private final ProductService services;

    public ProductController(ProductService services) {
        this.services = services;
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<ProductResponseDTO> create(@Valid ProductRequestDTO product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(services.createProduct(product));
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<ProductResponseDTO> findById(Long id) {
        return  ResponseEntity.ok(services.findProductById(id));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<ProductResponseDTO>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(services.findAllProductsPage(pageable));
    }

    @PutMapping(value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<ProductResponseDTO> update(Long id, @Valid ProductRequestDTO product) {
        return ResponseEntity.ok(services.updateProductById(id, product));
    }

    @PatchMapping(value = "/fields/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<ProductResponseDTO> updatePatch(Long id, Map<String, Object> fields) {
        return ResponseEntity.ok(services.updateProductField(fields, id));
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> delete(Long id) {
        services.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
