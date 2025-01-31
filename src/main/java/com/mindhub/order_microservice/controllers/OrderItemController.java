package com.mindhub.order_microservice.controllers;

import com.mindhub.order_microservice.config.JwtUtils;
import com.mindhub.order_microservice.dtos.OrderItemDTO;
import com.mindhub.order_microservice.dtos.ProductQuantityDTO;
import com.mindhub.order_microservice.exceptions.CustomException;
import com.mindhub.order_microservice.services.OrderItemService;
import com.mindhub.order_microservice.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/orderItems")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "Get all order items by order ID", description = "Retrieve all items for a specific order using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order items retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    @GetMapping("/user/{orderId}")
    public ResponseEntity<Set<OrderItemDTO>> getAllOrderItemsByOrderId(@PathVariable Long orderId) throws CustomException {
        Set<OrderItemDTO> orderItems = orderItemService.getAllOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }


    @Operation(summary = "Create a new order item", description = "Registers a new order item in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order item created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
    })
    @PostMapping("/user/{orderId}")
    public ResponseEntity<OrderItemDTO> addOrderItem(@PathVariable Long orderId, @RequestBody ProductQuantityDTO newOrderItem, HttpServletRequest request) throws CustomException {
        Long userId = jwtUtils.getIdFromToken(request.getHeader("Authorization"));
        OrderItemDTO orderItemRecord = orderItemService.addOrderItem(userId, orderId, newOrderItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemRecord);
    }

    @Operation(summary = "Delete an order item by ID", description = "Deletes an existing order item by the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order item deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Order item not found.")
    })
    @DeleteMapping("/user/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId, HttpServletRequest request) throws CustomException {
        Long userId = jwtUtils.getIdFromToken(request.getHeader("Authorization"));
        orderItemService.deleteOrderItem(userId, orderItemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update the quantity of an order item", description = "Updates the quantity of a specific order item by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid quantity provided."),
            @ApiResponse(responseCode = "404", description = "Order item not found.")
    })
    @PutMapping("/user/{orderItemId}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(@PathVariable Long orderItemId, @RequestBody Integer quantity, HttpServletRequest request) throws CustomException {
        Long userId = jwtUtils.getIdFromToken(request.getHeader("Authorization"));
        OrderItemDTO orderItems = orderItemService.updateOrderItemQuantity(userId, orderItemId, quantity);
        return ResponseEntity.ok(orderItems);
    }

}
