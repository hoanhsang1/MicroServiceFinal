package com.vti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vti.entity.Product;

public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
}