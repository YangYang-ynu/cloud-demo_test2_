package org.example.service;


import org.order.bean.Order;

public interface orderService {
    Order createOrder(Long productId, Long userId);
}
