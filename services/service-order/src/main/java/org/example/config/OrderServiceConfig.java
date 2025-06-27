package org.example.config;

import feign.Logger;
import feign.RetryableException;
import feign.Retryer;
import jdk.jfr.Category;
import jdk.jfr.Label;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import reactor.util.retry.Retry;

@Configuration
public class OrderServiceConfig {

    @Bean
    Retryer retryer (){
        return new Retryer.Default();

    }

    @LoadBalanced
    @Bean
    public RestTemplate RestTemplate() {
        return new RestTemplate();
    }
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
