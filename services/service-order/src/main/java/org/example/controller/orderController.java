package org.example.controller;

import org.example.properties.OrderProperties;
import org.example.service.orderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.order.bean.Order;
@RefreshScope
@RestController
public class orderController {
    @Autowired
    private orderService orderService;
    @Autowired
    private OrderProperties orderProperties;
    @GetMapping("/config")
    public String config(){
        return "order timeout: " + orderProperties.getTimeout()+" auto-confirm: " + orderProperties.getAutoConfirm() +
                "order dburl"+ orderProperties.getDbUrl()

                ;
    }



    @GetMapping("/order")


    public Order order(@RequestParam("productId") Long productId,@RequestParam("userId") Long userId) {
        Order order =orderService.createOrder(productId,userId);
        return order;
    }

}

