package com.mindhub.order_microservice.services;

import com.mindhub.order_microservice.dtos.*;
import com.mindhub.order_microservice.exceptions.CustomException;
import com.mindhub.order_microservice.models.OrderStatus;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<OrderDTO> getAllOrders();
    Set<OrderDTO> getAllOrdersByUserId(Long id);
    OrderDTO getOrderByUserId(Long userId, Long orderId) throws CustomException;
    OrderDTO getOrderById(Long id) throws CustomException;
    void deleteOrder(Long id) throws CustomException;
    boolean existsOrder(Long id);
    OrderCreatedDTO createOrder(NewOrderDTO newOrder) throws CustomException;
    OrderDTO changeStatus(Long userId,String userMail,Long orderId, OrderStatus orderStatus) throws CustomException;
    OrderItemDTO addOrderItem(Long userId, Long OrderId, ProductQuantityDTO productQuantityDTO) throws CustomException;
    boolean existsOrderItem(Long id);
    OrderItemDTO updateOrderItemQuantity(Long userId, Long orderItemId, Integer quantity) throws CustomException;
    void deleteOrderItem(Long userId, Long orderItemId) throws CustomException;
    Set<OrderItemDTO> getAllOrderItemsByOrderId(Long id) throws CustomException;
    void deleteOrderUser(Long userId, Long orderId) throws CustomException;


}
