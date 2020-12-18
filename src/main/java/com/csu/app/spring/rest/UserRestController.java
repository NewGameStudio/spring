package com.csu.app.spring.rest;

import com.csu.app.spring.dto.ProductDto;
import com.csu.app.spring.model.Product;
import com.csu.app.spring.model.User;
import com.csu.app.spring.security.jwt.JwtTokenProvider;
import com.csu.app.spring.service.ProductService;
import com.csu.app.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user/")
public class UserRestController {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public UserRestController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }


    @ResponseBody
    @GetMapping("products")
    public ResponseEntity<List<ProductDto>> listProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        List<ProductDto> productDtoList = ProductDto.fromProductsList(user.getProducts());

        return ResponseEntity.ok(productDtoList);
    }

    @ResponseBody
    @GetMapping("purchased")
    public ResponseEntity<List<ProductDto>> listPurchasedProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        List<ProductDto> productDtoList = ProductDto.fromProductsList(user.getPurchasedProducts());

        return ResponseEntity.ok(productDtoList);
    }


    @ResponseBody
    @GetMapping("products/add")
    public ResponseEntity<List<ProductDto>> addProduct(@RequestParam Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Product product = productService.findById(id);

        if(product == null)
            return ResponseEntity.badRequest().build();

        User user = userService.findByUsername(username);

        List<Product> products = user.getProducts();
        products.add(product);

        user.setProducts(products);

        userService.updateUser(user);

        return ResponseEntity.ok(ProductDto.fromProductsList(user.getProducts()));
    }

    @ResponseBody
    @GetMapping("products/delete")
    public ResponseEntity<List<ProductDto>> deleteProduct(@RequestParam Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        List<Product> products = user.getProducts();
        for (int i = 0; i < products.size(); i++) {
            if(products.get(i).getId() == id) {
                products.remove(i);
                break;
            }
        }

        user.setProducts(products);

        userService.updateUser(user);

        return ResponseEntity.ok(ProductDto.fromProductsList(user.getProducts()));
    }


    @ResponseBody
    @GetMapping("products/purchase")
    public ResponseEntity<List<ProductDto>> purchaseProduct(@RequestParam Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        Product purchasedProduct = null;

        List<Product> products = user.getProducts();
        List<Product> purchasedProducts = user.getPurchasedProducts();
        for(Product product : products) {
            if (product.getId() == id) {
                purchasedProduct = product;
                break;
            }
        }

        if(purchasedProduct == null)
            return ResponseEntity.badRequest().build();

        products.remove(purchasedProduct);
        purchasedProducts.add(purchasedProduct);

        user.setProducts(products);
        user.setPurchasedProducts(purchasedProducts);

        userService.updateUser(user);

        return ResponseEntity.ok(ProductDto.fromProductsList(user.getPurchasedProducts()));
    }
}
