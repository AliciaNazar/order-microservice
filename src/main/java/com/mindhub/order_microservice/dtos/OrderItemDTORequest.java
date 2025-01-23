package com.mindhub.order_microservice.dtos;

import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.OrderItemEntity;

public class OrderItemDTORequest {

    private OrderDTO order;
    private Long productId;
    private Integer quantity;

    public OrderItemDTORequest() {
    }

    public OrderItemDTORequest(OrderItemEntity orderItemEntity) {
        this.order = new OrderDTO(orderItemEntity.getOrderEntity());
        this.productId = orderItemEntity.getProductId();
        this.quantity = orderItemEntity.getQuantity();
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

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


    public static OrderItemEntity toEntity(OrderItemDTORequest orderItemDTORequest, OrderEntity orderEntity){
        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setProductId(orderItemDTORequest.getProductId());
        orderItem.setQuantity(orderItemDTORequest.getQuantity());
        orderItem.setOrderEntity(orderEntity);
        return orderItem;
    }



}
