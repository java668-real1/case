package com.janey668.consumer.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {
        "com.janey668.consumer.feign"
})
public class FeignClientConfig {
}
