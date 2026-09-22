package com.vti.form;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductFormUpdate {
    @NotBlank 
    @Size(max = 150, message = "Product name must not exceed 150 characters")
    private String name;
    private String description;
    @NotNull 
    @PositiveOrZero 
    private BigDecimal price;
    @PositiveOrZero 
    private Integer quantity;
    @Size (max = 100, message = "Category must not exceed 100 characters")
    private String category;
}