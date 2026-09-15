package com.vti.client.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderClientDto {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private String status; 
}