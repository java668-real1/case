package com.janey668.c4;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Thread.sleep;

@Slf4j
public class FutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        m1();
//        m2();
//        m3();
    }

    private static void m3() throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventExecutors = group.next();
        DefaultPromise promise = new DefaultPromise(eventExecutors);
        eventExecutors.execute(() -> {
            promise.setSuccess("执行成功");
            try {
                sleep(5000);
            } catch (Exception e) {
                promise.setFailure(e);
            }
            log.info("==========");
//            promise.setSuccess("执行成功");
        });

        String result = (String) promise.get();
        log.info("获得结果: {}", result);
    }

    private static void m2() throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventExecutors = group.next();
        io.netty.util.concurrent.Future<Integer> future = eventExecutors.submit(() -> {
            log.info("FutureTest");
            sleep(1000);
            return 1;
        });
        Integer result = future.get();
        log.info("获得结果: {}", result);
    }

    private static void m1() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> future = executorService.submit(() -> {
            int i = 1/0;
            log.info("FutureTest");
            sleep(1000);
            return 1;
        });
        Integer result = future.get();
        log.info("获得结果: {}", result);
    }
}
