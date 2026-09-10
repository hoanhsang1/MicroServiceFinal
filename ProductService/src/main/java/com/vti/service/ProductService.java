package com.vti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.vti.dto.ProductDto;
import com.vti.entity.Product;
import com.vti.entity.enums.ProductStatus;
import com.vti.form.ProductForm;
import com.vti.repository.IProductRepository;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    private ProductDto toDto(Product p) {
        return ProductDto.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .quantity(p.getQuantity())
                .category(p.getCategory())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    @Override
    public ProductDto createProduct(ProductForm form) {
        Product product = Product.builder()
                .name(form.getName())
                .description(form.getDescription())
                .price(form.getPrice())
                .quantity(form.getQuantity() != null ? form.getQuantity() : 0)
                .category(form.getCategory())
                .status(ProductStatus.AVAILABLE)
                .build();
        return toDto(productRepository.save(product));
    }

    @Override
    public List<ProductDto> searchProducts(String category, ProductStatus status, String name) {
        Specification<Product> spec = (root, query, cb) -> cb.conjunction();

        if (category != null && !category.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (name != null && !name.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        return productRepository.findAll(spec).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm id=" + id));
        return toDto(product);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductForm form) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm id=" + id));

        if (form.getName() != null) product.setName(form.getName());
        if (form.getDescription() != null) product.setDescription(form.getDescription());
        if (form.getPrice() != null) product.setPrice(form.getPrice());
        if (form.getCategory() != null) product.setCategory(form.getCategory());

        return toDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm id=" + id));
        product.setStatus(ProductStatus.INACTIVE); // xoá mềm
        productRepository.save(product);
    }

    @Override
    public ProductDto updateQuantity(Long id, Integer delta) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm id=" + id));

        int newQuantity = product.getQuantity() + delta;
        if (newQuantity < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số lượng tồn kho không đủ");
        }
        product.setQuantity(newQuantity);
        if (newQuantity == 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        } else if (product.getStatus() == ProductStatus.OUT_OF_STOCK) {
            product.setStatus(ProductStatus.AVAILABLE);
        }
        return toDto(productRepository.save(product));
    }
}