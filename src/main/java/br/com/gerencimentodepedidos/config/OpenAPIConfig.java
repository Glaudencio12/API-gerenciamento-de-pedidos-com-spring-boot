package br.com.gerencimentodepedidos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Project: Order management API for a restaurant")
                .version("v1")
                .description("This API simulates a basic order management system in a restaurant, including functionalities such as creating products (foods), opening new orders and adding items to them. In addition, it provides features for editing, searching and deleting products, orders and order items.")
        );
    }
}
