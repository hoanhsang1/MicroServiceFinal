package com.vti.client.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductClientDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String status; // AVAILABLE, OUT_OF_STOCK, INACTIVE
}