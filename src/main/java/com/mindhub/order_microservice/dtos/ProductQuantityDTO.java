package com.mindhub.order_microservice.dtos;

public class ProductQuantityDTO { // lo que el cliente solicita
    private Long id;
    private Integer quantity;

    public ProductQuantityDTO() {
    }

    public ProductQuantityDTO(Long id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
