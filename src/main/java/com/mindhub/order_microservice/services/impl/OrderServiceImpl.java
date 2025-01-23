package com.mindhub.order_microservice.services.impl;

import com.mindhub.order_microservice.dtos.OrderDTO;
import com.mindhub.order_microservice.dtos.OrderDTORequest;
import com.mindhub.order_microservice.exceptions.CustomException;
import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.OrderItemEntity;
import com.mindhub.order_microservice.repositories.OrderRepository;
import com.mindhub.order_microservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderDTO createOrder(OrderDTORequest orderDTORequest) {
        idUserValidations(orderDTORequest.getUserId());

        OrderEntity order = new OrderEntity();
        order.setUserId(orderDTORequest.getUserId());
        order.setStatus(orderDTORequest.getStatus());
        order = this.orderRepository.save(order);
        return new OrderDTO(order);
    }

    @Override
    public List<OrderDTO> getOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(order -> new OrderDTO(order))
                .toList();
        return orderDTOs;
    }

    @Override
    public OrderDTO updateOrderStatus(Long id, OrderDTORequest orderDTORequest) {
        idValidation(id);

        if( this.orderRepository.existsById(id)){
            OrderEntity order = this.orderRepository.findById(id)
                    .orElseThrow();
            order.setStatus(orderDTORequest.getStatus());
            order = this.orderRepository.save(order);
            return new OrderDTO(order);
        }else{
            throw new CustomException("Order not found.", HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public void deleteOrder(Long id){
        idValidation(id);

        if( this.orderRepository.existsById(id)) {
            this.orderRepository.deleteById(id);
        }else{
            throw new CustomException("Order not found.", HttpStatus.NOT_FOUND);
        }
    }


    private void idUserValidations(Long id){
        if (id<=0){
            throw new CustomException("Invalid User id.");
        }
    }

    private void idValidation(Long id){
        if (id == null || id <= 0){
            throw new CustomException("Invalid id.");
        }
    }

}



