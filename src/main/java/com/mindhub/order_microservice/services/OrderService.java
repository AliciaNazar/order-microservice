package com.mindhub.order_microservice.services;

import com.mindhub.order_microservice.dtos.NewOrderDTO;
import com.mindhub.order_microservice.dtos.OrderDTO;
import com.mindhub.order_microservice.dtos.OrderDTORequest;
import com.mindhub.order_microservice.exceptions.CustomException;

import java.util.List;

public interface OrderService {

    //OrderDTO createOrder(OrderDTORequest orderDTORequest);
    List<OrderDTO> getOrders();
    OrderDTO updateOrderStatus(Long id, OrderDTORequest orderDTORequest);
    void deleteOrder(Long id);



    OrderDTO createOrder(NewOrderDTO newOrder) throws CustomException;

}
