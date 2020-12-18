package com.csu.app.spring.service.impl;

import com.csu.app.spring.model.Product;
import com.csu.app.spring.repository.ProductsRepository;
import com.csu.app.spring.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductsRepository productsRepository;

    @Autowired
    public ProductServiceImpl(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public Product findById(Long id) {
        return productsRepository.findById(id).orElse(null);
    }

    @Override
    public Product findByName(String name) {
        return productsRepository.findByName(name);
    }

    @Override
    public List<Product> getAll() {
        return productsRepository.findAll();
    }
}
