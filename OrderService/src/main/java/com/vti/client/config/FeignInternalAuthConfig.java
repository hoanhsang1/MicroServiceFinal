package com.vti.client.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignInternalAuthConfig {

    @Value("${internal.api.key}")
    private String internalApiKey;

    @Bean
    public RequestInterceptor internalApiKeyInterceptor() {
        return template -> template.header("X-Internal-Api-Key", internalApiKey);
    }
}