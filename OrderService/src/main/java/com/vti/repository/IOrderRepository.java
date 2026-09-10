package com.vti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vti.entity.Order;

public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}