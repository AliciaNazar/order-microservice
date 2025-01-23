package com.mindhub.order_microservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handlerBusinessException(CustomException ex){
        return new ResponseEntity<>(ex.getMessage(),ex.getStatus());
    }
}
