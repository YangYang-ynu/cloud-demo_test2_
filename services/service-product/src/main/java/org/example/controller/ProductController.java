package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.service.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import product.bean.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j

@RestController
public class ProductController {
    @Autowired
    private productService productService;

    // 模拟产品数据库，用于演示REST操作
    private static final Map<Long, Product> productDatabase = new HashMap<>();
    
    static {
        // 初始化一些产品数据
        for (long i = 1; i <= 5; i++) {
            Product product = new Product();
            product.setId(i);
            product.setProductName("产品" + i);
            product.setPrice(new BigDecimal(100.0 * i));
            product.setSum(10 * (int)i);
            productDatabase.put(i, product);
        }
    }

    //查询商品数据
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") Long productId, HttpServletRequest request) {
        System.out.println("hello");
        System.out.println(request.getDateHeader("X-Token"));
        Product product= productService.getProductById(productId);

        return product;
    }
    
    // 新增方法 - 获取所有产品
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return new ArrayList<>(productDatabase.values());
    }
    
    // POST方法 - 创建新产品
    @PostMapping("/product")
    public Product createProduct(@RequestBody Product product) {
        Long id = (long) (productDatabase.size() + 1);
        product.setId(id);
        productDatabase.put(id, product);
        return product;
    }
    
    // PUT方法 - 更新产品
    @PutMapping("/product/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (!productDatabase.containsKey(id)) {
            return null; // 或者抛出异常
        }
        
        product.setId(id);
        productDatabase.put(id, product);
        return product;
    }
    
    // DELETE方法 - 删除产品
    @DeleteMapping("/product/{id}")
    public Map<String, Object> deleteProduct(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        if (!productDatabase.containsKey(id)) {
            response.put("success", false);
            response.put("message", "产品不存在，无法删除");
            return response;
        }
        
        Product deletedProduct = productDatabase.remove(id);
        response.put("success", true);
        response.put("message", "成功删除产品");
        response.put("deletedProduct", deletedProduct);
        return response;
    }
}
