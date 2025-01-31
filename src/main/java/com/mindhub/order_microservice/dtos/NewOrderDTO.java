package com.mindhub.order_microservice.dtos;

import java.util.Set;

public class NewOrderDTO {
    private String email; //SACARLE EL EMAIL
    private Set<ProductQuantityDTO> productSet;

    public NewOrderDTO() {
    }

    public NewOrderDTO(String email, Set<ProductQuantityDTO> productSet) {
        this.email = email;
        this.productSet = productSet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<ProductQuantityDTO> getProductSet() {
        return productSet;
    }

    public void setProductSet(Set<ProductQuantityDTO> productSet) {
        this.productSet = productSet;
    }
}
