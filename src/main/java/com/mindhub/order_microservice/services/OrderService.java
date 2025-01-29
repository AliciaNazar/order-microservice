package com.mindhub.order_microservice.services;

import com.mindhub.order_microservice.dtos.*;
import com.mindhub.order_microservice.exceptions.CustomException;
import com.mindhub.order_microservice.models.OrderStatus;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<OrderDTO> getAllOrders();
    Set<OrderDTO> getAllOrdersByUserId(Long id);
    OrderDTO getOrderById(Long id) throws CustomException;
    OrderDTO updateOrderStatus(Long id, OrderDTORequest orderDTORequest);
    void deleteOrder(Long id) throws CustomException;
    boolean existsOrder(Long id);
    OrderCreatedDTO createOrder(NewOrderDTO newOrder) throws CustomException;
    OrderDTO changeStatus(Long id, OrderStatus orderStatus) throws CustomException;
    OrderItemDTO addOrderItem(Long OrderId, ProductQuantityDTO productQuantityRecord) throws CustomException;
    boolean existsOrderItem(Long id);
    OrderItemDTO updateOrderItemQuantity(Long id, Integer quantity) throws CustomException;
    void deleteOrderItem(Long id) throws CustomException;
    Set<OrderItemDTO> getAllOrderItemsByOrderId(Long id) throws CustomException;

}
