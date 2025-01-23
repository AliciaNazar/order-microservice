package com.mindhub.order_microservice.controllers;

import com.mindhub.order_microservice.dtos.OrderItemDTO;
import com.mindhub.order_microservice.dtos.OrderItemDTORequest;
import com.mindhub.order_microservice.services.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Operation(summary = "Create a new order item", description = "Registers a new order item in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order item created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
    })
    @PostMapping("/orderItems")
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItemDTORequest orderItemDTORequest){
        OrderItemDTO orderItemDTO = this.orderItemService.createOrderItem(orderItemDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemDTO);
    }

    @Operation(summary = "Delete an order item by ID", description = "Deletes an existing order item by the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order item deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Order item not found.")
    })
    @DeleteMapping("/orderItems/{id}")
        public ResponseEntity<?> deleteOrderItem(@PathVariable("id") Long id){
            this.orderItemService.deleteOrderItem(id);
            return ResponseEntity.noContent().build();
        }
}
