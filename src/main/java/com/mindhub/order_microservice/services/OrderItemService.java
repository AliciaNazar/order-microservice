package com.mindhub.order_microservice.services;

import com.mindhub.order_microservice.dtos.OrderItemDTO;
import com.mindhub.order_microservice.dtos.OrderItemDTORequest;

public interface OrderItemService {

    OrderItemDTO createOrderItem(OrderItemDTORequest orderItemDTORequest);
    void deleteOrderItem(Long id);
}
