package com.example.productcheckout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productcheckout.model.Cart;

@Repository
public interface ProductCheckoutRepository extends JpaRepository<Cart, Integer> {

}
