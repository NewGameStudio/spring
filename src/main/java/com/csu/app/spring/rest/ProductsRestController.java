package com.csu.app.spring.rest;

import com.csu.app.spring.dto.ProductDto;
import com.csu.app.spring.model.Product;
import com.csu.app.spring.model.User;
import com.csu.app.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/products/")
public class ProductsRestController {

    private final ProductService productService;

    @Autowired
    public ProductsRestController(ProductService productService) {
        this.productService = productService;
    }

    @ResponseBody
    @GetMapping("all")
    public ResponseEntity<List<ProductDto>> listAllProducts() {
        List<Product> products = productService.getAll();

        List<ProductDto> productDtoList = new ArrayList<>();
        products.forEach(product -> productDtoList.add(ProductDto.fromProduct(product)));

        return ResponseEntity.ok(productDtoList);
    }
}
