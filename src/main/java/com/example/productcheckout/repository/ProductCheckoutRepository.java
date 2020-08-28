package com.example.productentry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productentry.model.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {

}
