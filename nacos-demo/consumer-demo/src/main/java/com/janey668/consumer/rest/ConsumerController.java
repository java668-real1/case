package com.janey668.consumer.rest;

import com.janey668.consumer.feign.NacosRestFULAppServiceFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ConsumerController {

    private final NacosRestFULAppServiceFeign nacosRestFULAppServiceFeign;

    @GetMapping("/test")
    public String test(String name, HttpServletRequest request){
        System.out.println("app client name = " + name);
        System.out.println("客户端ip：" + request.getRemoteAddr());
        System.out.println("开始调用远程服务");
        String result = nacosRestFULAppServiceFeign.test(name);
        System.out.println("远程服务返回：" + result);
        return result;
    }

}
