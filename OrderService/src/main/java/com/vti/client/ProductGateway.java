package com.vti.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.vti.client.dto.ProductClientDto;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Component
public class ProductGateway {

    @Autowired
    private ProductClient productClient;

    @CircuitBreaker(name = "productService", fallbackMethod = "fetchProductFallback")
    public ProductClientDto fetchProduct(Long productId) {
        return productClient.getProductById(productId);
    }

    public ProductClientDto fetchProductFallback(Long productId, Throwable t) {
        if (t instanceof FeignException.NotFound) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Sản phẩm id=" + productId + " không tồn tại");
        }
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "ProductService hiện không khả dụng, vui lòng thử lại sau");
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "adjustQuantityFallback")
    public void adjustQuantity(Long productId, Integer delta) {
        productClient.updateQuantity(productId, delta);
    }

    public void adjustQuantityFallback(Long productId, Integer delta, Throwable t) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "ProductService hiện không khả dụng, vui lòng thử lại sau");
    }
}