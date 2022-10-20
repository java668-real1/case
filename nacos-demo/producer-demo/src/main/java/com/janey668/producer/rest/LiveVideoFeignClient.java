package com.janey668.producer.rest;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.janey668.producer.constant.NacosConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequiredArgsConstructor
public class LiveVideoFeignClient {

    private final NacosDiscoveryProperties nacosDiscoveryProperties;
    private final NacosAutoServiceRegistration nacosAutoServiceRegistration;
    private final NacosRegistration registration;

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
    public String put(String threadNum) throws NacosException {
        String serviceId = registration.getServiceId();
        String group = this.nacosDiscoveryProperties.getGroup();
        Instance instance = this.getNacosInstanceFromRegistration(registration);
        nacosDiscoveryProperties.getMetadata().put(NacosConstant.THREAD_NUM, threadNum);
        nacosDiscoveryProperties.namingServiceInstance().registerInstance(serviceId, group, instance);
        return threadNum;
    }

    private Instance getNacosInstanceFromRegistration(Registration registration) {
        Instance instance = new Instance();
        instance.setIp(registration.getHost());
        instance.setPort(registration.getPort());
        instance.setWeight((double)this.nacosDiscoveryProperties.getWeight());
        instance.setClusterName(this.nacosDiscoveryProperties.getClusterName());
        instance.setMetadata(registration.getMetadata());
        return instance;
    }
}
