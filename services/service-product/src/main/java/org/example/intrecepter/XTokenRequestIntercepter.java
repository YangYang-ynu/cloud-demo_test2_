package org.example.intrecepter;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.UUID;

public class XTokenRequestIntercepter implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header("X-Token", UUID.randomUUID().toString());

    }
}
