package com.mindhub.order_microservice.dtos;

import com.mindhub.order_microservice.models.ProductError;

public class ErrorProductDTO {
    private Long id;
    private ProductError productError;

    public ErrorProductDTO() {
    }

    public ErrorProductDTO(Long id, ProductError productError) {
        this.id = id;
        this.productError = productError;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductError getProductError() {
        return productError;
    }

    public void setProductError(ProductError productError) {
        this.productError = productError;
    }
}
