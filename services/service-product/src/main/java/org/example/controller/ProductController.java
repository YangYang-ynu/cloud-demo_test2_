package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.service.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import product.bean.Product;
@Slf4j

@RestController
public class ProductController {
    @Autowired
    private productService productService;

    //查询商品数据
    @GetMapping("/product/{id}")

    public Product getProduct(@PathVariable("id") Long productId, HttpServletRequest request) {
        System.out.println("hello");
        System.out.println(request.getDateHeader("X-Token"));
        Product product= productService.getProductById(productId);

        return product ;
    }
}
