package br.com.gerencimentodepedidos.mocks;

import br.com.gerencimentodepedidos.data.dto.ProductDTO;
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

    public List<ProductDTO> mockListProductsDTO(){
        List<ProductDTO> products = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            products.add(mockProductDTO(i));
        }
        return products;
    }


    public Product mockProductEntity(Integer number){
        Product product = new Product();
        product.setId(Long.valueOf(number));
        product.setName("Name Test " + number);
        ProductCategory[] categories = ProductCategory.values();
        product.setCategory(categories[(int)(number % categories.length)]);
        product.setPrice((double) number + 10);
        return product;
    }

    public ProductDTO mockProductDTO(Integer number){
        ProductDTO product = new ProductDTO();
        product.setId(Long.valueOf(number));
        product.setName("Name Test " + number);
        ProductCategory[] categories = ProductCategory.values();
        product.setCategory(categories[(int)(number % categories.length)]);
        product.setPrice((double) number + 10);
        return product;
    }
}
