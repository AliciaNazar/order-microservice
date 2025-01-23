package com.mindhub.order_microservice.controllers;


import com.mindhub.order_microservice.dtos.OrderDTO;
import com.mindhub.order_microservice.dtos.OrderDTORequest;
import com.mindhub.order_microservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTORequest orderDTORequest){
        OrderDTO orderDTO = this.orderService.createOrder(orderDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getOrders(){
        List<OrderDTO> orders = this.orderService.getOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable("id") Long id,
            @RequestBody OrderDTORequest orderDTORequest){
        OrderDTO orderUpdated = this.orderService.updateOrderStatus(id, orderDTORequest);
        return ResponseEntity.ok(orderUpdated);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id){
        this.orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }


}
