package com.vti.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.vti.entity.enums.OrderStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class OrderDto {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String shippingAddress;
    private List<OrderItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}