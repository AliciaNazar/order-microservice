package com.mindhub.order_microservice.repositories;

import com.mindhub.order_microservice.models.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    List<OrderEntity> findByUserId(Long id);
}
