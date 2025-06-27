package product.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private String productName;
    private BigDecimal price;
    private Long id;
    private int sum;
}
