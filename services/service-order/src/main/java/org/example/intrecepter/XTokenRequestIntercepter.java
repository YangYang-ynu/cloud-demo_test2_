package org.example.intrecepter;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.UUID;

public class XTokenRequestIntercepter implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        System.out.println("XTokenRequestIntercepter启动");
        template.header("X-Token", UUID.randomUUID().toString());

    }
}
