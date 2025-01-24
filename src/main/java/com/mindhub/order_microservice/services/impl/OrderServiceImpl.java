package com.mindhub.order_microservice.services.impl;

import com.mindhub.order_microservice.dtos.*;
import com.mindhub.order_microservice.exceptions.CustomException;
import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.OrderItemEntity;
import com.mindhub.order_microservice.models.OrderStatus;
import com.mindhub.order_microservice.repositories.OrderItemRepository;
import com.mindhub.order_microservice.repositories.OrderRepository;
import com.mindhub.order_microservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${USERS_PATH}")
    private String userPath;

    @Value("${PRODUCTS_PATH}")
    private String productPath;

//    @Override
//    public OrderDTO createOrder(OrderDTORequest orderDTORequest) {
//        idUserValidations(orderDTORequest.getUserId());
//
//        OrderEntity order = new OrderEntity();
//        order.setUserId(orderDTORequest.getUserId());
//        order.setStatus(orderDTORequest.getStatus());
//        order = this.orderRepository.save(order);
//        return new OrderDTO(order);
//    }

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






    @Override
    public OrderDTO createOrder(NewOrderDTO newOrder) throws CustomException {
        String uri = "/email/" + newOrder.getEmail();
        try {
            // Llamo a UserService para obtener el userId
            Long userId = restTemplate.getForObject(userPath + uri, Long.class);

            // Llamar a ProductService para verificar productos
                // envío un conjunto de productos (ProductQuantityDTO)
                // recibo en la respuesta Set<ExistentProductDTO> con info procesada por el servicio
            ParameterizedTypeReference<Set<ExistentProductDTO>> responseType =
                    new ParameterizedTypeReference<>() {};
            HttpEntity<Set<ProductQuantityDTO>> httpEntity = new HttpEntity<>(newOrder.getProductSet());
            ResponseEntity<Set<ExistentProductDTO>> responseEntity = restTemplate.exchange(
                    productPath, HttpMethod.PUT, httpEntity, responseType);

            // Creo la orden y asigno usuario y productos
            OrderEntity order = new OrderEntity(userId,null,OrderStatus.PENDING);
            orderRepository.save(order);

            // Genero la lista de OrderItems
            generateOrderItemSet(responseEntity.getBody(), order);

            // Guardo la orden con los ítems ahora
            orderRepository.save(order);

            return new OrderDTO(order);
        } catch (Exception e) {
            throw new CustomException("Error creating order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void generateOrderItemSet(Set<ExistentProductDTO> products, OrderEntity order) {
        Set<OrderItemEntity> orderItems = new HashSet<>();
        for (ExistentProductDTO product : products) {
            OrderItemEntity orderItem = new OrderItemEntity(order, product.getId(), product.getQuantity());
            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }
        order.setOrderItemList(orderItems);
    }

}



