package com.mindhub.order_microservice.dtos;

import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.OrderStatus;

import java.util.List;

public class OrderDTORequest {

    private Long userId;
    //private List<OrderItemDTO> products;
    private OrderStatus status;


    public OrderDTORequest() {
    }

    public OrderDTORequest(OrderEntity orderEntity) {
        this.userId = orderEntity.getUserId();
//        this.products = orderEntity.getProducts()
//                .stream()
//                .map(orderItemEntity -> new OrderItemDTO(orderItemEntity))
//                .toList();
        this.status = orderEntity.getStatus();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

//    public List<OrderItemDTO> getProducts() {
//        return products;
//    }
//
//    public void setProducts(List<OrderItemDTO> products) {
//        this.products = products;
//    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
