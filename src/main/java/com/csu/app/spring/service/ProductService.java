package com.csu.app.spring.service;

import com.csu.app.spring.model.Product;

import java.util.List;

public interface ProductService {

    Product findById(Long id);

    Product findByName(String name);

    List<Product> getAll();
}
