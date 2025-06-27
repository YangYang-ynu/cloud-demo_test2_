package org.example.service.impl;

import org.example.service.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product.bean.Product;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
public class productServiceImpl implements productService {


    @Override
    public Product getProductById(Long productId) {
        Product product=new Product();
        product.setId(productId);
        product.setSum(5);
        product.setProductName("火腿肠");
        product.setPrice(new BigDecimal("1.5"));
        return product ;

    }
}
