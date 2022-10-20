package com.janey668.producer.config;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.discovery.NacosWatch;
import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.janey668.producer.constant.NacosConstant;
import com.janey668.producer.properties.NacosWatchProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

/**
 * nacos metadata
 *
 * @author wusiwei
 */
@Configuration
@AllArgsConstructor
@ConditionalOnNacosDiscoveryEnabled
@EnableConfigurationProperties(NacosDiscoveryProperties.class)
@AutoConfigureBefore({SimpleDiscoveryClientAutoConfiguration.class, CommonsClientAutoConfiguration.class})
public class NacosClientConfiguration {

//    private final NacosDiscoveryProperties nacosWatchProperties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "spring.cloud.nacos.discovery.watch.enabled", matchIfMissing = true)
    public NacosWatch nacosWatch(NacosDiscoveryProperties properties, ObjectProvider<TaskScheduler> taskScheduler) {
        properties.getMetadata().put(NacosConstant.THREAD_NUM, "nacosWatchProperties.getThreadNum()");
        return new NacosWatch(properties, taskScheduler);
    }

}
