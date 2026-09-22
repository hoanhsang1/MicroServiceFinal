package com.vti.form;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderFormUpdate {
    @Size (max = 255, message = "Shipping address must not exceed 255 characters")
    private String shippingAddress;
    // đổi status nên tách API riêng (PUT /orders/{id}/status), không gộp vào đây
}