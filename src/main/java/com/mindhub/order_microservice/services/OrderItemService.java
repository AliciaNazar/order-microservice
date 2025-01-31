package com.mindhub.order_microservice.services;

import com.mindhub.order_microservice.dtos.OrderItemDTO;
import com.mindhub.order_microservice.dtos.OrderItemDTORequest;
import com.mindhub.order_microservice.dtos.ProductQuantityDTO;
import com.mindhub.order_microservice.exceptions.CustomException;

import java.util.Set;

public interface OrderItemService {

//    OrderItemDTO createOrderItem(OrderItemDTORequest orderItemDTORequest);
//    void deleteOrderItem(Long id);



    Set<OrderItemDTO> getAllOrderItemsByOrderId(Long id) throws CustomException;
    OrderItemDTO addOrderItem(Long userId, Long orderId, ProductQuantityDTO productQuantityRecord) throws CustomException;
    void deleteOrderItem(Long userId, Long orderItemId) throws CustomException;
    OrderItemDTO updateOrderItemQuantity(Long userId, Long orderItemId, Integer quantity) throws CustomException;
    boolean existsOrderItem(Long id);

}
