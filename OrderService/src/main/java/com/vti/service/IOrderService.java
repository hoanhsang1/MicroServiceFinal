package com.vti.service;

import java.util.List;

import com.vti.dto.OrderDto;
import com.vti.entity.enums.OrderStatus;
import com.vti.form.OrderForm;
import com.vti.form.OrderFormUpdate;

public interface IOrderService {
    OrderDto createOrder(OrderForm form);
    OrderDto getOrderById(Long id);
    List<OrderDto> getOrdersByUserId(Long userId);
    OrderDto updateOrderStatus(Long id, OrderStatus status);
    OrderDto updateOrder(Long id, OrderFormUpdate form);
    void cancelOrder(Long id); // chỉ cho phép khi PENDING
}