package com.csu.app.spring.repository;

import com.csu.app.spring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

}
