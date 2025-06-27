package org.order.bean;

import lombok.Data;
import product.bean.Product;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Order {
    private long id;
    private BigDecimal totalPrice;
    private Long userId;
    private String nickname;
    private String address;
    private List<Product> products;

}

