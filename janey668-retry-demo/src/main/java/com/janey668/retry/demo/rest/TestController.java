package com.janey668.retry.demo.rest;

import com.janey668.retry.demo.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Jerry.chen
 * @desc
 * @date 2022/06/14 17:10
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    private final ThreadPoolExecutor messageConsumeDynamicExecutor;

    @RequestMapping("test")
    public String test() {
        log.info("save...");
        messageConsumeDynamicExecutor.execute(() -> {
            testService.test();
        });
        return "success";
    }

    @RequestMapping("test1")
    public String test1() {
        log.info("save...");
        testService.test();
        return "success";
    }
}