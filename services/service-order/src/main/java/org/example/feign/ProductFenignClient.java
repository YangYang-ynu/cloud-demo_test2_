package org.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import product.bean.Product;

@FeignClient(value = "service-product",fallback = org.example.feign.fallback.ProductFeignClient.class)
public interface ProductFenignClient {
    //标在 FeignClient 发请求
    @GetMapping("/product/{id}")
    Product getProductById(@PathVariable("id") Long id );

}
