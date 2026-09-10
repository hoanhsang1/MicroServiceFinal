package com.vti.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.vti.entity.enums.ProductStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String category;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}