package com.janey668.retry.demo.service.impl;

import com.janey668.retry.demo.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jerry.chen
 * @desc
 * @date 2022/06/14 17:13
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    @Retryable(maxAttempts = 5, backoff = @Backoff(value = 3 * 60 * 1000 , maxDelay = 5 * 60 * 1000L,multiplier = 2D))
    public void test() {
        log.info("重试次数：{}", count.getAndIncrement());
        int i = 1/0;


    }

}