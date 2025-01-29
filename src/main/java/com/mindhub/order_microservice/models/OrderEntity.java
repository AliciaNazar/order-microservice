package com.mindhub.order_microservice.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @OneToMany(mappedBy = "orderEntity")
    private Set<OrderItemEntity> orderItemList = new HashSet<>();
    private OrderStatus status = OrderStatus.PENDING;

    public OrderEntity() {
    }

    public OrderEntity(Long userId, Set<OrderItemEntity> orderItemList, OrderStatus status) {
        this.userId = userId;
        this.orderItemList = orderItemList;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<OrderItemEntity> getProducts() {
        return orderItemList;
    }

    public Set<OrderItemEntity> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(Set<OrderItemEntity> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }


    public void addOrderItem(OrderItemEntity orderItem){
        orderItemList.add(orderItem);
        orderItem.setOrderEntity(this);
    }
}
