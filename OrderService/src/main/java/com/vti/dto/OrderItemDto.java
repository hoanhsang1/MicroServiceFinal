package com.vti.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class OrderItemDto {
    private Long id;
    private Long productId;
    private String productName;   // lấy từ ProductService lúc tạo, lưu snapshot tên để không mất nếu sản phẩm bị xoá sau này
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}