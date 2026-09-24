package com.vti.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.vti.client.dto.OrderClientDto;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Component
public class OrderGateway {

    @Autowired
    private OrderClient orderClient;

    @CircuitBreaker(name = "orderService", fallbackMethod = "getOrderFallback")
    public OrderClientDto getOrder(Long orderId) {
        return orderClient.getOrderById(orderId);
    }

    public OrderClientDto getOrderFallback(Long orderId, Throwable t) {
        if (t instanceof FeignException.NotFound) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Đơn hàng id=" + orderId + " không tồn tại");
        }
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "OrderService hiện không khả dụng, vui lòng thử lại sau");
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "updateStatusFallback")
    public void updateStatus(Long orderId, Map<String, String> body) {
        orderClient.updateStatus(orderId, body);
    }

    public void updateStatusFallback(Long orderId, Map<String, String> body, Throwable t) {
        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                "Không thể cập nhật trạng thái đơn hàng id=" + orderId);
    }
}