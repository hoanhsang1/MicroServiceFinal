package com.vti.service;

import java.util.List;

import com.vti.dto.ProductDto;
import com.vti.entity.enums.ProductStatus;
import com.vti.form.ProductForm;

public interface IProductService {
    ProductDto createProduct(ProductForm form);
    List<ProductDto> searchProducts(String category, ProductStatus status, String name);
    ProductDto getProductById(Long id);
    ProductDto updateProduct(Long id, ProductForm form);
    void deleteProduct(Long id); // set INACTIVE
    ProductDto updateQuantity(Long id, Integer delta);
}