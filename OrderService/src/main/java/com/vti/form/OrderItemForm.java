package com.vti.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemForm {
    @NotNull 
    private Long productId;
    @NotNull
    @Positive 
    private Integer quantity;
    // price/subtotal KHÔNG nhận từ client — service tự lấy giá thật từ ProductService để tránh gian lận giá
}