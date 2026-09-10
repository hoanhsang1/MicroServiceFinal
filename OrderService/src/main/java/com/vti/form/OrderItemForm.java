package com.vti.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemForm {
    private Long productId;
    private Integer quantity;
    // price/subtotal KHÔNG nhận từ client — service tự lấy giá thật từ ProductService để tránh gian lận giá
}