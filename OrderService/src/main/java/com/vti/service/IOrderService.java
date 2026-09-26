package com.vti.service;

import java.util.List;

import com.vti.dto.OrderDto;
import com.vti.entity.enums.OrderStatus;
import com.vti.form.OrderForm;
import com.vti.form.OrderFormUpdate;

public interface IOrderService {
    OrderDto createOrder(OrderForm form);
    List<OrderDto> getOrdersByUserId(Long userId);
    OrderDto updateOrder(Long id, OrderFormUpdate form, Long currentUserId, String currentUserRole);
    void cancelOrder(Long id, Long currentUserId, String currentUserRole); // chỉ cho phép khi PENDING
    OrderDto getOrderById(Long id, Long currentUserId, String currentUserRole, boolean internalCall);
    OrderDto updateOrderStatus(Long id, OrderStatus status, String currentUserRole, boolean internalCall);
}