package com.vti.form;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderForm {
    private Long userId;
    private String shippingAddress;
    private List<OrderItemForm> items;
}