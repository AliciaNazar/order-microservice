package com.mindhub.order_microservice.dtos;

public class ExistentProductDTO { //lo que me devuelve el PUT que hice a products
    private Long id;
    private Double price;
    private Integer quantity;

    public ExistentProductDTO() {
    }

    public ExistentProductDTO(Long id, Double price, Integer quantity) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
