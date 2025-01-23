package com.mindhub.order_microservice.controllers;

import com.mindhub.order_microservice.dtos.OrderItemDTO;
import com.mindhub.order_microservice.dtos.OrderItemDTORequest;
import com.mindhub.order_microservice.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @PostMapping("/orderItems")
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItemDTORequest orderItemDTORequest){
        OrderItemDTO orderItemDTO = this.orderItemService.createOrderItem(orderItemDTORequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemDTO);
    }

    @DeleteMapping("/orderItems/{id}")
        public ResponseEntity<?> deleteOrderItem(@PathVariable("id") Long id){
            this.orderItemService.deleteOrderItem(id);
            return ResponseEntity.noContent().build();
        }
}
