package com.mindhub.order_microservice.dtos;

import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.OrderStatus;
import java.util.ArrayList;
import java.util.List;


public class OrderDTO {

    private Long id;
    private Long userId;
    private List<OrderItemDTO> products = new ArrayList<>();
    private OrderStatus status;

    public OrderDTO() {
    }

    public OrderDTO(OrderEntity orderEntity) {
        this.id = orderEntity.getId();
        this.userId = orderEntity.getUserId();
        this.products = orderEntity.getProducts()
                .stream()
                .map(orderItemEntity -> new OrderItemDTO(orderItemEntity))
                .toList();
        this.status = orderEntity.getStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemDTO> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItemDTO> products) {
        this.products = products;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }


    public static OrderEntity toEntity(OrderDTO orderDTO){
        OrderEntity order = new OrderEntity();
        order.setStatus(orderDTO.getStatus());
        order.setUserId(orderDTO.getUserId());
        return order;
    }

}
