package br.com.gerencimentodepedidos.unitTests.mocks;

import br.com.gerencimentodepedidos.data.dto.request.ProductRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.ProductResponseDTO;
import br.com.gerencimentodepedidos.enums.ProductCategory;
import br.com.gerencimentodepedidos.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MockProduct {
    public List<Product> mockListProducts(){
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            products.add(mockProductEntity(i));
        }
        return products;
    }

    public Product mockProductEntity(Integer number){
        if (number == null) {
            throw new IllegalArgumentException("Number cannot be null");
        }
        
        Product product = new Product();
        product.setId(Long.valueOf(number));
        product.setName("Name Test " + number);
        ProductCategory[] categories = ProductCategory.values();
        product.setCategory(categories[(int)(number % categories.length)]);
        product.setPrice((double) number + 10);
        return product;
    }

    public ProductRequestDTO mockProductDTORequest(Integer number){
        if (number == null) {
            throw new IllegalArgumentException("Number cannot be null");
        }
        
        ProductRequestDTO product = new ProductRequestDTO();
        product.setId(Long.valueOf(number));
        product.setName("Name Test " + number);
        ProductCategory[] categories = ProductCategory.values();
        product.setCategory(categories[(int)(number % categories.length)]);
        product.setPrice((double) number + 10);
        return product;
    }

    public List<ProductRequestDTO> mockListProductsDTO(){
        List<ProductRequestDTO> products = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            products.add(mockProductDTORequest(i));
        }
        return products;
    }

    public ProductResponseDTO mockProductDTOResponse(Integer number){
        if (number == null) {
            throw new IllegalArgumentException("Number cannot be null");
        }

        ProductResponseDTO product = new ProductResponseDTO();
        product.setId(Long.valueOf(number));
        product.setName("Name Test " + number);
        ProductCategory[] categories = ProductCategory.values();
        product.setCategory(categories[(int)(number % categories.length)]);
        product.setPrice((double) number + 10);
        return product;
    }
}
