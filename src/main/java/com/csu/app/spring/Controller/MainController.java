package com.csu.app.spring.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/index")
    public String root() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/products")
    public String products() {
        return "products";
    }

    @GetMapping("/user_cart")
    public String cart() {
        return "user_cart";
    }

    @GetMapping("/purchased")
    public String purchased() {
        return "purchased";
    }
}
