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
    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<OrderItemEntity> products = new HashSet<>();
    private OrderStatus status = OrderStatus.PENDING;

    public OrderEntity() {
    }

    public OrderEntity(Long userId, List<OrderItemEntity> products, OrderStatus status) {
        this.userId = userId;
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
        return products;
    }

    public void setProducts(Set<OrderItemEntity> products) {
        this.products = products;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
