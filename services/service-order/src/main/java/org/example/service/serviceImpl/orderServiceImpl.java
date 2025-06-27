package org.example.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.feign.ProductFenignClient;
import org.example.service.orderService;
import org.order.bean.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import product.bean.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
@Slf4j

@Service
public class orderServiceImpl implements orderService {
    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    ProductFenignClient productFenignClient;

    @Override
    public Order createOrder(Long productId, Long userId) {

        Product product= productFenignClient.getProductById(productId);

//        Product product=getProductFormRemoteWithAnnotation(productId);
        Order order =new Order();
        order.setId(1L);
        order.setNickname("yangyang");
        order.setUserId(userId);
        order.setTotalPrice(product.getPrice().multiply(new BigDecimal(product.getSum())));

        order.setProducts(Arrays.asList(product));
        return order;
    }
    private Product getProductFormRemote(Long productId){
        //获取商品服务
        List< ServiceInstance >instances=discoveryClient.getInstances("service-product");
        ServiceInstance instance=instances.get(0);
        String url="http://"+instance.getHost()+":"+instance.getPort()+"/product/"+productId;

        //2.发送请求向远程

        log.info("远程请求{}",url);
        return restTemplate.getForObject(url, Product.class);//把json自动转为javabean
    }
    private Product getProductFormRemoteWithLoadBanlance(Long productId){

        ServiceInstance instance = loadBalancerClient.choose("service-product");
        //获取商品服务

        String url="http://"+instance.getHost()+":"+instance.getPort()+"/product/"+productId;

        //2.发送请求向远程

        log.info("远程请求{}",url);
        return restTemplate.getForObject(url, Product.class);//把json自动转为javabean
    }

    private Product getProductFormRemoteWithAnnotation(Long productId){

        //使用注解这里会被动态替换：service-product
        String url="http://service-product/product/"+productId;
        log.info("远程请求{}",url);
        return restTemplate.getForObject(url, Product.class);//把json自动转为javabean
    }
}

