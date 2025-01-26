package com.mindhub.order_microservice.controllers;


import com.mindhub.order_microservice.dtos.NewOrderDTO;
import com.mindhub.order_microservice.dtos.OrderCreatedDTO;
import com.mindhub.order_microservice.dtos.OrderDTO;
import com.mindhub.order_microservice.dtos.OrderDTORequest;
import com.mindhub.order_microservice.exceptions.CustomException;
import com.mindhub.order_microservice.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

//    @Operation(summary = "Create a new order", description = "Registers a new order in the system.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Order created successfully."),
//            @ApiResponse(responseCode = "400", description = "Invalid input data."),
//    })
//    @PostMapping("/orders")
//    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTORequest orderDTORequest){
//        OrderDTO orderDTO = this.orderService.createOrder(orderDTORequest);
//        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
//    }

    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully."),
    })
    @GetMapping()
    public ResponseEntity<List<OrderDTO>> getOrders(){
        List<OrderDTO> orders = this.orderService.getOrders();
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Update the status of an order", description = "Updates the status of a specific order identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully."),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable("id") Long id,
            @RequestBody OrderDTORequest orderDTORequest){
        OrderDTO orderUpdated = this.orderService.updateOrderStatus(id, orderDTORequest);
        return ResponseEntity.ok(orderUpdated);
    }

    @Operation(summary = "Delete an order by ID", description = "Deletes an existing order by the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id){
        this.orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }







    @Operation(summary = "Create a new order", description = "Creates a new order based on the provided order details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid order details provided."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping
    public ResponseEntity<OrderCreatedDTO> createOrder(@RequestBody NewOrderDTO newOrder) throws CustomException {
        OrderCreatedDTO createdOrder = orderService.createOrder(newOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

}
