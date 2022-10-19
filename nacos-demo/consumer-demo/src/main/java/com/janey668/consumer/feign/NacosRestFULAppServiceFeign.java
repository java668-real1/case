package com.janey668.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("producer-demo")
public interface NacosRestFULAppServiceFeign {

    @GetMapping("/sayHello")
    String test(@RequestParam("name") String name);

}
