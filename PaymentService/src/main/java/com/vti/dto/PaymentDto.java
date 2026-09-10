package com.vti.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.vti.entity.enums.PaymentMethod;
import com.vti.entity.enums.PaymentStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class PaymentDto {
    private Long id;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionCode;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}