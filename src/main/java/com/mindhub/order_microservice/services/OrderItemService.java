package com.mindhub.order_microservice.services;

import com.mindhub.order_microservice.dtos.OrderItemDTO;
import com.mindhub.order_microservice.dtos.OrderItemDTORequest;
import com.mindhub.order_microservice.exceptions.CustomException;

import java.util.Set;

public interface OrderItemService {

    OrderItemDTO createOrderItem(OrderItemDTORequest orderItemDTORequest);
    void deleteOrderItem(Long id);

}
