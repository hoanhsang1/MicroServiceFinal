package com.vti.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.vti.client.dto.ProductClientDto;

// "product-service" phải KHỚP với spring.application.name bên ProductService
@FeignClient(name = "ProductService", configuration = com.vti.client.config.FeignInternalAuthConfig.class)
public interface ProductClient {

    @GetMapping("/api/v1/products/{id}")
    ProductClientDto getProductById(@PathVariable("id") Long id);

    @PatchMapping("/api/v1/products/{id}/quantity")
    ProductClientDto updateQuantity(@PathVariable("id") Long id, @RequestBody Integer delta);
}