package com.janey668.producer.rest;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.janey668.producer.constant.NacosConstant;
import com.janey668.producer.properties.NacosWatchProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RefreshScope
@RestController
@RequiredArgsConstructor
public class LiveVideoFeignClient {

//    private final NacosWatchProperties nacosWatchProperties;

//    @PostConstruct
//    public void init() {
//        nacosDiscoveryProperties.getMetadata().put("threadNum", "11");
//    }

    @GetMapping("/sayHello")
    public String sayHello(String name){
        System.out.println("provider controller name = " + name);
        return name;
    }


    @GetMapping("/put")
    public String put(String threadNum){
//        nacosWatchProperties.setThreadNum(threadNum);
        return threadNum;
    }

}
