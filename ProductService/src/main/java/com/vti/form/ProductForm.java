package com.vti.form;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductForm {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String category;
}