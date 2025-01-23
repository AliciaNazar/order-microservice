package com.mindhub.order_microservice.dtos;

import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.OrderItemEntity;

public class OrderItemDTO {

    private Long id;
    //private OrderDTO order;
    private Long productId;
    private Integer quantity;

    public OrderItemDTO() {
    }

    public OrderItemDTO(OrderItemEntity orderItemEntity) {
        this.id = orderItemEntity.getId();
        //this.order = new OrderDTO(orderItemEntity.getOrderEntity());
        this.productId = orderItemEntity.getProductId();
        this.quantity = orderItemEntity.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public OrderDTO getOrder() {
//        return order;
//    }
//
//    public void setOrder(OrderDTO order) {
//        this.order = order;
//    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
