package com.janey668.producer.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LiveVideoFeignClient {

    @GetMapping("/sayHello")
    public String sayHello(String name){
        System.out.println("provider controller name = " + name);
        return name;
    }

}
