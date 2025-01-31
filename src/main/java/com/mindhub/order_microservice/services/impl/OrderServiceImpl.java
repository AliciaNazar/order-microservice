package com.mindhub.order_microservice.services.impl;

import com.mindhub.order_microservice.dtos.*;
import com.mindhub.order_microservice.dtos.ProductDTO;
import com.mindhub.order_microservice.events.*;
import com.mindhub.order_microservice.exceptions.CustomException;
import com.mindhub.order_microservice.models.OrderEntity;
import com.mindhub.order_microservice.models.OrderItemEntity;
import com.mindhub.order_microservice.models.OrderStatus;
import com.mindhub.order_microservice.models.ProductError;
import com.mindhub.order_microservice.repositories.OrderItemRepository;
import com.mindhub.order_microservice.repositories.OrderRepository;
import com.mindhub.order_microservice.services.OrderItemService;
import com.mindhub.order_microservice.services.OrderService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService, OrderItemService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${USERS_PATH}")
    private String userPath;

    @Value("${ADMIN_PATH}")
    private String adminPath;

    @Value("${PRODUCTS_PATH}")
    private String productPath;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Set<OrderDTO> getAllOrders() {
        List<OrderEntity> orderEntityList = orderRepository.findAll();
        Set<OrderDTO> orderDTOSet = orderEntityList.stream()
                .map(orderDto -> new OrderDTO(orderDto))
                .collect(Collectors.toSet());
        return  orderDTOSet;
    }

    @Override
    public Set<OrderDTO> getAllOrdersByUserId(Long id) {
        List<OrderEntity> orderEntityList = orderRepository.findByUserId(id);
        Set<OrderDTO> orderDTOs = orderEntityList.stream()
                .map(orderDto -> new OrderDTO(orderDto))
                .collect(Collectors.toSet());
        return  orderDTOs;
    }

    @Override
    public OrderDTO getOrderById(Long id) throws CustomException {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new CustomException("Order not found.", HttpStatus.NOT_FOUND));
        OrderDTO orderDTO = new OrderDTO(order);
        return orderDTO;
    }

    @Override
    public OrderDTO getOrderByUserId(Long userId, Long orderId) throws CustomException {
        OrderDTO order = getOrderById(orderId);
        validateOrderOwner(userId,order.getUserId());
        return order;
    }


    private void idValidation(Long id){
        if (id == null || id <= 0){
            throw new CustomException("Invalid id.");
        }
    }

    private Long getUserIdFromEmail(String email) throws CustomException {
        try{
            String url = adminPath + "/email/" + email;
            Long userId = restTemplate.getForObject(url, Long.class);
            return userId;
        } catch (RestClientException e) {
            if (e instanceof HttpStatusCodeException){
                HttpStatusCodeException aux = (HttpStatusCodeException)e;
                throw new CustomException("User not found.", (HttpStatus) aux.getStatusCode());
            }else{
                throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }




    @Override
    @Transactional(rollbackFor = {Exception.class})
    public OrderCreatedDTO createOrder(NewOrderDTO newOrder) throws CustomException {
        Long userId = getUserIdFromEmail(newOrder.getEmail());
        HashMap<Long,Integer> existentProductMap = getExistentProducts(newOrder.getProductSet().stream().toList());

        OrderEntity order = new OrderEntity(userId,null, OrderStatus.PENDING);

        orderRepository.save(order);

        List<ErrorProductDTO> orderItemsError = setOrderItemList(existentProductMap, newOrder.getProductSet().stream().toList(), order);

        orderRepository.save(order);
        try {
            updateProducts(order.getOrderItemList().stream().toList(), -1);
        }catch (Exception e){
            throw new CustomException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        OrderDTO orderDTO = new OrderDTO(order);
        OrderCreatedDTO orderCreated = new OrderCreatedDTO(orderDTO, orderItemsError);

        return orderCreated;
    }




    private HashMap<Long,Integer> getExistentProducts(List<ProductQuantityDTO> productQuantityRecordList) throws CustomException {
        ParameterizedTypeReference<HashMap<Long, Integer>> responseType =
                new ParameterizedTypeReference<>() {};
        HttpEntity<List<ProductQuantityDTO>> httpEntity = new HttpEntity<>(productQuantityRecordList);
        try{
            ResponseEntity<HashMap<Long, Integer>> responseEntity = restTemplate.exchange(productPath, HttpMethod.PUT ,httpEntity, responseType);
            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw new CustomException("Error communicating with product-service", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private List<ErrorProductDTO> setOrderItemList(HashMap<Long, Integer> existentProducts, List<ProductQuantityDTO> wantedProducts, OrderEntity order){
        List<OrderItemEntity> orderItemList = new ArrayList<>();
        List<ErrorProductDTO> errorProductList = new ArrayList<>();
        wantedProducts.forEach(wantedProduct -> {
            if (existentProducts.containsKey(wantedProduct.getId())){
                Integer realQuantity = existentProducts.get(wantedProduct.getId());
                if (realQuantity>= wantedProduct.getQuantity()){
                    OrderItemEntity orderItem = new OrderItemEntity(order,wantedProduct.getId(),wantedProduct.getQuantity());
                    orderItemRepository.save(orderItem);
                    orderItemList.add(orderItem);
                }else{
                    errorProductList.add(new ErrorProductDTO(wantedProduct.getId(), ProductError.NO_STOCK));
                }
            }else{
                errorProductList.add(new ErrorProductDTO(wantedProduct.getId(), ProductError.NOT_FOUND));
            }
        });

        Set<OrderItemEntity> orderItemSet = new HashSet<>(orderItemList);
        order.setOrderItemList(orderItemSet);

        return errorProductList;
    }

    private void updateProducts(List<OrderItemEntity> orderItemList, int factor) throws CustomException {

        List<ProductQuantityDTO> productQuantityRecordList = new ArrayList<>();
        orderItemList.forEach(orderItem -> {
            productQuantityRecordList.add(new ProductQuantityDTO(orderItem.getProductId(), factor*orderItem.getQuantity()));
        });

        HttpEntity<List<ProductQuantityDTO>> httpEntity = new HttpEntity<>(productQuantityRecordList);

        try{
            restTemplate.exchange(productPath + "/to-order", HttpMethod.PUT ,httpEntity, String.class);
        } catch (RestClientException e) {
            throw new CustomException("Error communicating with product-service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @Override
    public OrderDTO changeStatus(Long userId,String userMail,Long orderId, OrderStatus orderStatus) throws CustomException {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found.", HttpStatus.NOT_FOUND));
        validateOrderOwner(userId,order.getUserId());
        order.setStatus(orderStatus);
        order = orderRepository.save(order);
        if (order.getStatus() == OrderStatus.COMPLETED){
            sendDataToGeneratePdf(order);
        }
        return new OrderDTO(order);
    }


    private void sendDataToGeneratePdf(OrderEntity order){
        List<ProductDTO> listProducts = new ArrayList<>();
        for (OrderItemEntity item : order.getOrderItemList()){
            try {
                ProductDTO product = restTemplate.getForObject(productPath + "/" + item.getProductId(), ProductDTO.class );
                product.setQuantity(item.getQuantity());
                listProducts.add(product);
            }catch (RestClientException e){
                //throw new CustomException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        String email = restTemplate.getForObject(userPath+"/"+order.getUserId(),String.class);
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(order.getId(),email,listProducts);

        rabbitTemplate.convertAndSend("email-exchange", "user.pdf", orderCreatedEvent);
    }

    @Override
    public void deleteOrder(Long id) throws CustomException {
        if (existsOrder(id)){
            orderRepository.deleteById(id);
        }else{
            throw new CustomException("Order not found.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteOrderUser(Long userId, Long orderId) throws CustomException{
        OrderDTO order = getOrderById(orderId);
        validateOrderOwner(userId,order.getUserId());
        deleteOrder(orderId);
    }

    @Override
    public boolean existsOrder(Long id) {
        return orderRepository.existsById(id);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public OrderItemDTO addOrderItem(Long userId, Long OrderId, ProductQuantityDTO productQuantityDTO) throws CustomException {
        OrderEntity order = orderRepository.findById(OrderId).orElseThrow(() -> new CustomException("Order not found.", HttpStatus.NOT_FOUND));
        validateOrderOwner(userId,order.getUserId());
        validOrderStatus(order.getId());
        validateOrderItem(order.getId(),productQuantityDTO.getId());
        if (productQuantityDTO.getQuantity()<0){
            throw new CustomException("The quantity provide its invalid.");
        }

        List<ProductQuantityDTO> auxList = new ArrayList<>();
        auxList.add(productQuantityDTO);

        HashMap<Long, Integer> existentProductMap = getExistentProducts(auxList);

        if (existentProductMap.containsKey(productQuantityDTO.getId())){
            Integer realQuantity = existentProductMap.get(productQuantityDTO.getId());
            if (realQuantity>= productQuantityDTO.getQuantity()){
                OrderItemEntity orderItem = new OrderItemEntity(order,productQuantityDTO.getId(),productQuantityDTO.getQuantity());

                List<OrderItemEntity> orderItemList = new ArrayList<>();
                orderItemList.add(orderItem);
                try {
                    updateProducts(orderItemList, -1);
                }catch (Exception e){
                    throw new CustomException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
                }
                orderItemRepository.save(orderItem);
                order.addOrderItem(orderItem);
                orderRepository.save(order);

                return new OrderItemDTO(orderItem);
            }else{
                throw new CustomException("Not enough stock.", HttpStatus.NOT_FOUND);
            }
        }else{
            throw new CustomException("Product not found.", HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Set<OrderItemDTO> getAllOrderItemsByOrderId(Long id) throws CustomException {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new CustomException("Order not found.", HttpStatus.NOT_FOUND));
        Set<OrderItemDTO> orderItemSet = order.getOrderItemList().stream()
                .map(orderItem -> new OrderItemDTO(orderItem))
                .collect(Collectors.toSet());
        return orderItemSet;
    }

    private void validateOrderItem(Long orderId,Long orderItemProductId) throws CustomException {
        Set<OrderItemDTO> orderItemSet = getAllOrderItemsByOrderId(orderId);
        Iterator<OrderItemDTO> it = orderItemSet.iterator();
        while (it.hasNext()){
            if (it.next().getId()==orderItemProductId){
                throw new CustomException("The product its already into the order, you may want to change the quantity");
            }
        }
    }

    @Override
    public void deleteOrderItem(Long userId, Long orderItemId) throws CustomException {
        OrderItemEntity orderItem = orderItemRepository.findById(orderItemId).orElseThrow(()->new CustomException("Order item not found.", HttpStatus.NOT_FOUND));
        validateOrderOwner(userId,orderItem.getOrderEntity().getUserId());
        validOrderStatus(orderItem.getOrderEntity().getId());

        List<OrderItemEntity> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        updateProducts(orderItemList,1);

        orderItemRepository.delete(orderItem);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public OrderItemDTO updateOrderItemQuantity(Long userId, Long orderItemId, Integer quantity) throws CustomException {
        OrderItemEntity orderItem = orderItemRepository.findById(orderItemId).orElseThrow(()->new CustomException("Order item not found.", HttpStatus.NOT_FOUND));
        validateOrderOwner(userId,orderItem.getOrderEntity().getUserId());
        validOrderStatus(orderItem.getOrderEntity().getId());

        int difference = orderItem.getQuantity()-quantity;

        if (quantity>0 && orderItem.getQuantity() != quantity){
            HashMap<Long, Integer> existentProduct = getExistentProducts(List.of(new ProductQuantityDTO(orderItem.getProductId(),quantity)));

            try {
                if (difference > 0) {
                    updateProducts(List.of(new OrderItemEntity(null, orderItem.getProductId(), difference)), 1);
                } else {
                    if (existentProduct.get(orderItem.getProductId()) >= -1 * difference) {
                        updateProducts(List.of(new OrderItemEntity(null, orderItem.getProductId(), difference)), 1);
                    } else {
                        throw new CustomException("Not enough stock.", HttpStatus.NOT_ACCEPTABLE);
                    }
                }
            }catch (Exception e){
                throw new CustomException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
            orderItem.setQuantity(quantity);
            orderItem = orderItemRepository.save(orderItem);
            return new OrderItemDTO(orderItem);
        }else{
            throw new CustomException("The quantity provide is invalid", HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @Override
    public boolean existsOrderItem(Long id) {
        return orderItemRepository.existsById(id);
    }

    private void validOrderStatus(Long id) throws CustomException {
        OrderDTO order = getOrderById(id);
        if (order.getStatus()==OrderStatus.COMPLETED){
            throw new CustomException("The order must be pending to update the order item.",HttpStatus.UNAUTHORIZED);
        }
    }



    private void validateOrderOwner(Long userId, Long userOrderId){
        if (userId != userOrderId) {
            throw new CustomException("The order is not from this user");
        }
    }




}



