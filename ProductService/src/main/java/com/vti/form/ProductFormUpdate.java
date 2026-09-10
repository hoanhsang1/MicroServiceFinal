package com.vti.form;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductFormUpdate {
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
}