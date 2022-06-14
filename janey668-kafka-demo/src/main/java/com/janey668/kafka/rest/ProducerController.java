package com.janey668.kafka.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ProducerController {

    /**
     * Kafka 模板用来向 kafka 发送数据
     */
    private final KafkaTemplate<String, String> kafka;

    /**
     * 普通异步发送
     *
     * @param msg
     * @return
     */
    @RequestMapping("/test1")
    public String test1(String msg) {
        kafka.send("first", msg);
        return "ok";
    }

    /**
     * 同步发送
     *
     * @param msg
     * @return
     */
    @RequestMapping("/test2")
    public String test2(String msg) {
        kafka.send("first", msg);
        return "ok";
    }

    /**
     * 带回调函数的异步发送
     *
     * @param msg
     * @return
     */
    @RequestMapping("/test3")
    public String test3(String msg) {
        kafka.send("first", msg);
        return "ok";
    }
}
