package com.vti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import com.vti.dto.ProductDto;
import com.vti.entity.enums.ProductStatus;
import com.vti.form.ProductForm;
import com.vti.service.IProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody @Valid ProductForm form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(form));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(productService.searchProducts(category, status, name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody @Valid ProductForm form) {
        return ResponseEntity.ok(productService.updateProduct(id, form));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // OrderService sẽ gọi API này khi làm phần liên service (delta âm khi trừ kho lúc đặt hàng)
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<ProductDto> updateQuantity(@PathVariable Long id, @RequestBody Integer delta) {
        return ResponseEntity.ok(productService.updateQuantity(id, delta));
    }
}