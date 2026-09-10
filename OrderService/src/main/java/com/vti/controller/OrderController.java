package com.vti.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vti.dto.OrderDto;
import com.vti.entity.enums.OrderStatus;
import com.vti.form.OrderForm;
import com.vti.form.OrderFormUpdate;
import com.vti.service.IOrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody OrderForm form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(form));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable Long id, @RequestBody OrderFormUpdate form) {
        return ResponseEntity.ok(orderService.updateOrder(id, form));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        OrderStatus status = OrderStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}