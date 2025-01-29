package com.mindhub.order_microservice.services.impl;

import com.mindhub.order_microservice.dtos.OrderDTO;
import com.mindhub.order_microservice.dtos.OrderItemDTO;
import com.mindhub.order_microservice.dtos.OrderItemDTORequest;
import com.mindhub.order_microservice.dtos.ProductQuantityDTO;
import com.mindhub.order_microservice.exceptions.CustomException;
import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.OrderItemEntity;
import com.mindhub.order_microservice.repositories.OrderItemRepository;
import com.mindhub.order_microservice.repositories.OrderRepository;
import com.mindhub.order_microservice.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService{

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderItemDTO createOrderItem(OrderItemDTORequest orderItemDTORequest) {
        inputValidations(orderItemDTORequest);

        OrderEntity order = this.orderRepository.findById(orderItemDTORequest.getOrder().getId())
                        .orElseThrow();
        OrderItemEntity orderItem = OrderItemDTORequest.toEntity(orderItemDTORequest,order);
        orderItem = this.orderItemRepository.save(orderItem);
        return new OrderItemDTO(orderItem);
    }

    @Override
    public void deleteOrderItem(Long id) {
        idValidation(id);

        if(this.orderItemRepository.existsById(id)){
            this.orderItemRepository.deleteById(id);
        }else{
            throw new CustomException("Order item not found.", HttpStatus.NOT_FOUND);
        }

    }


    private void idValidation(Long id){
        if (id == null || id <= 0){
            throw new CustomException("Invalid id.");
        }
    }

    private void inputValidations(OrderItemDTORequest orderItemDTORequest){
        productIdValidations(orderItemDTORequest.getProductId());
        quantityValidations(orderItemDTORequest.getQuantity());
    }

    private void productIdValidations(Long id){
        if (id <= 0){
            throw new CustomException("Invalid product id");
        }
    }

    private void quantityValidations(Integer quantity){
        if (quantity==null || quantity<=0){
            throw new CustomException("Quantity can't be zero or negative.");
        }
    }


}
