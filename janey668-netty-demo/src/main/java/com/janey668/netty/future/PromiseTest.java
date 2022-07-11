package com.janey668.netty.future;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class PromiseTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup executors = new NioEventLoopGroup();
        DefaultPromise<Integer> promise = new DefaultPromise(executors.next());

        executors.execute(() -> {
            log.info("execute...");
            try {
                Thread.sleep(1000);
                int i = 1 / 0;
                promise.setSuccess(100);
            } catch (Exception e) {
                promise.setFailure(e);
            }

        });
//        log.info("等待结果...");
//        if (promise.isSuccess()) {
//            Integer result = promise.getNow();
//            log.info("获取结果...{}, success:{}", result, promise.isSuccess());
//        } else {
//            log.error("执行报错：", promise.cause());
//        }
        promise.addListener((result) -> {
            if (result.isSuccess()) {
                Object now = result.getNow();
                log.info("获取结果...{}, success:{}", now);
            } else {
                log.error("执行报错:", result.cause());
            }
        });
        executors.shutdownGracefully();

    }

}
