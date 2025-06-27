package org.example.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "order")
//无需refreshscope即可自动刷新
public class OrderProperties {
    String timeout;
    String autoConfirm;
    String dbUrl;
}
