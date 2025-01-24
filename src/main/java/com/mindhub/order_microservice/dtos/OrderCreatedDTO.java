package com.mindhub.order_microservice.dtos;

import java.util.List;

public class OrderCreatedDTO {
    private OrderDTO orderDTO;
    private List<ErrorProductDTO> errorProductList;

    public OrderCreatedDTO() {
    }

    public OrderCreatedDTO(OrderDTO orderDTO, List<ErrorProductDTO> errorProductList) {
        this.orderDTO = orderDTO;
        this.errorProductList = errorProductList;
    }

    public OrderDTO getOrderDTO() {
        return orderDTO;
    }

    public void setOrderDTO(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }

    public List<ErrorProductDTO> getErrorProductList() {
        return errorProductList;
    }

    public void setErrorProductList(List<ErrorProductDTO> errorProductList) {
        this.errorProductList = errorProductList;
    }
}
