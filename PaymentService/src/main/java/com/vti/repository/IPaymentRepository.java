package com.vti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vti.entity.Payments;

public interface IPaymentRepository extends JpaRepository<Payments, Long> {
    List<Payments> findByOrderId(Long orderId);
    boolean existsByOrderIdAndUserId(Long orderId, Long userId);
}