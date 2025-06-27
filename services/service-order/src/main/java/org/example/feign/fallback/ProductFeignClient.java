package org.example.feign.fallback;

import org.example.feign.ProductFenignClient;
import org.springframework.stereotype.Component;
import product.bean.Product;
@Component
public class ProductFeignClient implements ProductFenignClient {
    @Override
    public Product getProductById(Long id) {
        System.out.println("兜底回调");
        return new Product();
    }
}
