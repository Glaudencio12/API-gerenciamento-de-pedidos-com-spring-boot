package br.com.gerencimentodepedidos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                description = "This API simulates a basic order management system in a restaurant, " +
                              "including functionalities such as creating products (foods), opening " +
                              "new orders and adding items to them. In addition, it provides features " +
                              "for editing, searching and deleting products, orders and order items.",
                contact = @Contact(
                        name = "Glaudencio",
                        email = "devglaudencio@gmail.com"
                ),
                version = "V2",
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = @Server(
                url = "http://localhost:8080",
                description = "Servidor de Desenvolvimento"
        )
)

public class OpenAPIConfig { }
