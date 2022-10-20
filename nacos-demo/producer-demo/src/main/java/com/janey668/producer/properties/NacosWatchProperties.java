package com.janey668.producer.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wusiwei
 */
@Data
@ConfigurationProperties(prefix = "framework.nacos.watch")
public class NacosWatchProperties {

    /**
     * 版本号
     */
    private String threadNum = "v1";

}
