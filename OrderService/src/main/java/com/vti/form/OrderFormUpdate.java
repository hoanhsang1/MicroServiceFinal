package com.vti.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderFormUpdate {
    private String shippingAddress;
    // đổi status nên tách API riêng (PUT /orders/{id}/status), không gộp vào đây
}