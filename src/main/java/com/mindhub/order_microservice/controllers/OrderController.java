package com.mindhub.order_microservice.controllers;


import com.mindhub.order_microservice.config.JwtUtils;
import com.mindhub.order_microservice.dtos.NewOrderDTO;
import com.mindhub.order_microservice.dtos.OrderCreatedDTO;
import com.mindhub.order_microservice.dtos.OrderDTO;
import com.mindhub.order_microservice.exceptions.CustomException;
import com.mindhub.order_microservice.models.OrderStatus;
import com.mindhub.order_microservice.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtils jwtUtils;


    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully."),
    })
    @GetMapping("/admin")
    public ResponseEntity<List<OrderDTO>> getOrders(){
        List<OrderDTO> orders = this.orderService.getAllOrders().stream().toList();
        return ResponseEntity.ok(orders);
    }

    //Retrieve all orders of a specific user
    @GetMapping("/user/all")
    public ResponseEntity<Set<OrderDTO>> getAllOrdersByUserId(HttpServletRequest request) {
        Long userId = jwtUtils.getIdFromToken(request.getHeader("Authorization"));
        Set<OrderDTO> orders = orderService.getAllOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    //Retrieve all orders of a specific user
    @GetMapping("/admin/all/{userId}")
    public ResponseEntity<Set<OrderDTO>> getAllOrdersByUserId(@PathVariable Long userId) {
        Set<OrderDTO> orders = orderService.getAllOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId, HttpServletRequest request) throws CustomException {
        Long userId = jwtUtils.getIdFromToken(request.getHeader("Authorization"));
        OrderDTO order = orderService.getOrderByUserId(userId,orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/admin/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) throws CustomException {
        OrderDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }



    @Operation(summary = "Create a new order", description = "Creates a new order based on the provided order details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid order details provided."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping("/user")
    public ResponseEntity<OrderCreatedDTO> createOrder(@RequestBody NewOrderDTO newOrder, HttpServletRequest request) throws CustomException {
        String email = jwtUtils.getEmailFromToken(request.getHeader("Authorization"));
        OrderCreatedDTO createdOrder = orderService.createOrder(newOrder);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }


    @Operation(summary = "Update the status of an order", description = "Updates the status of a specific order identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully."),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping("/user/{orderId}")
    public ResponseEntity<OrderDTO> changeStatus(@PathVariable Long orderId, @RequestBody OrderStatus orderStatus, HttpServletRequest request) throws CustomException {
        Long userId  = jwtUtils.getIdFromToken(request.getHeader("Authorization"));
        String email = jwtUtils.getEmailFromToken(request.getHeader("Authorization"));
        OrderDTO orderDTO = orderService.changeStatus(userId,email, orderId, orderStatus);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }


    @DeleteMapping("/user/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId, HttpServletRequest request) throws CustomException {
        Long userId  = jwtUtils.getIdFromToken(request.getHeader("Authorization"));
        orderService.deleteOrderUser(userId,orderId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete an order by ID", description = "Deletes an existing order by the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    @DeleteMapping("/admin/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id){
        this.orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }







}
