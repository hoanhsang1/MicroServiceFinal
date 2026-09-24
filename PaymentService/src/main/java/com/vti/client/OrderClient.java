package com.vti.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.vti.client.dto.OrderClientDto;

// "OrderService" phải KHỚP với spring.application.name bên OrderService
@FeignClient(name = "OrderService", configuration = com.vti.client.config.FeignInternalAuthConfig.class)
public interface OrderClient {

    @GetMapping("/api/v1/orders/{id}")
    OrderClientDto getOrderById(@PathVariable("id") Long id);

    @PutMapping("/api/v1/orders/{id}/status")
    OrderClientDto updateStatus(@PathVariable("id") Long id, @RequestBody Map<String, String> body);
}