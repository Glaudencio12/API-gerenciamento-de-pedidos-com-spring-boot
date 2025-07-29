package br.com.gerencimentodepedidos.controller.docs;

import br.com.gerencimentodepedidos.data.dto.request.ProductRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.ProductResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
public interface ProductControllerDocs {
    @Operation(summary = "Find a product", description = "Seeks a product for its respective ID", tags = "Product",
        responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "402", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    ProductResponseDTO findById(@Parameter(description = "Id of the product", example = "1") @PathVariable("id") Long id);

    @Operation(summary = "Find all products", description = "Search all registered products", tags = "Product",
        responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class)))
            ),
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    List<ProductResponseDTO> findAll();

    @Operation(summary = "Create a product", description = "Create a product with the following data: name, price and category", tags = "Product",
        responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    ProductResponseDTO create(@RequestBody @Valid ProductRequestDTO product);

    @Operation(summary = "Updates a product", description = "Update an product by their respective ID passing the updateProductById via Body", tags = "Product",
        responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))
            ),
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    ProductResponseDTO update(@Parameter(description = "Id of the product", example = "1") @PathVariable("id") Long id, @RequestBody @Valid ProductRequestDTO product);

    @Operation(summary = "Delete a product", description = "Delete a product by its respective ID", tags = "Product",
        responses = {
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    ResponseEntity<?> delete(@Parameter(description = "Id of the product", example = "1") @PathVariable("id") Long id);
}
