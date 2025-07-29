package br.com.gerencimentodepedidos.controller.docs;

import br.com.gerencimentodepedidos.data.dto.request.OrderItemRequestDTO;
import br.com.gerencimentodepedidos.data.dto.request.OrderRequestDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderItemResponseDTO;
import br.com.gerencimentodepedidos.data.dto.response.OrderResponseDTO;
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

public interface OrderControllerDocs {
    @Operation(summary = "Create a order", description = "Creates an order that will contain the requested items (products)", tags = "Order",

        responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
            ),
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    OrderResponseDTO create(@RequestBody @Valid OrderRequestDTO order);

    @Operation(summary = "Search an order", description = "Seek an order by your respective ID", tags = "Order",
        responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
            ),
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    OrderResponseDTO findById(@Parameter(description = "Id of the order", example = "1") @PathVariable("id") Long id);

    @Operation(summary = "Search all orders", description = "Search all requests made", tags = "Order",
        responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class)))
            ),
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    List<OrderResponseDTO> findAll();

    @Operation(summary = "Delete an order", description = "Delete an order", tags = "Order",
        responses = {
            @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        }
    )
    ResponseEntity<?> delete(@Parameter(description = "Id of the order", example = "1") @PathVariable("id") Long id);
}
