package br.com.gerencimentodepedidos.controller.docs;

import br.com.gerencimentodepedidos.data.dto.request.OrderItemRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderItemResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface OrderItemControllerDocs {
    @Operation(summary = "Create a order item", description = "Creates an item for an order", tags = {"Order Item"},
        responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                content = @Content(schema = @Schema(implementation = OrderItemRequestDTO.class))
            ),
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "402", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    OrderItemResponseDTO create(@PathVariable("orderId") Long orderId, @RequestBody @Valid OrderItemRequestDTO item);

    @Operation(summary = "Search an item", description = "Seek an item by your respective ID", tags = "Order Item",
        responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                content = @Content(schema = @Schema(implementation = OrderItemResponseDTO.class))
            ),
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "402", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    OrderItemResponseDTO findById(@PathVariable("itemId") Long id);

    @Operation(summary = "Search all item", description = "Search all items added to orders", tags = "Order Item",
        responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderItemRequestDTO.class)))
            ),
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "402", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    List<OrderItemResponseDTO> findAll();

    @Operation(summary = "Search an item", description = "Update an item by their respective ID passing the updateProductById via Body", tags = "Order Item",
        responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                content = @Content(schema = @Schema(implementation = OrderItemRequestDTO.class))
            ),
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "402", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    OrderItemResponseDTO update(@PathVariable("id") Long id, @RequestBody @Valid OrderItemRequestDTO item);

    @Operation(summary = "Delete an item", description = "Delete an item from an order", tags = "Order Item",
            responses = {
                    @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "402", content = @Content),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )

    ResponseEntity<?> delete(@PathVariable("id") Long id);
}
