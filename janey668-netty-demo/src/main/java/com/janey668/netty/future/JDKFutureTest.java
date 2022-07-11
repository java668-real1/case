package com.janey668.netty.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class JDKFutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> future = executorService.submit(() -> {
            log.info("submit ...");
            Thread.sleep(1000);
            int i = 1/0;
            return 100;
        });
        log.info("等待结果 ...");
        Integer result = future.get();
        log.info("获取结果 ...{}", result);
    }

}
