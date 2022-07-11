package com.janey668.netty.future;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class NettyFutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup executors = new NioEventLoopGroup();
        Future<Integer> future = executors.submit(() -> {
            log.info("submit ...");
            int i = 1/0;
            Thread.sleep(1000);
            return 100;
        });
        log.info("等待结果 ...");
//        Integer result = future.get();
//        log.info("获取结果 ...{}", result);
        future.addListener((result) -> {
            if (result.isSuccess()) {
                log.info("获取结果 ...{}", result.getNow());
            } else {
                log.error("任务执行异常 ...", result.cause());
            }
        });
        log.info("任务结束 ...");
        executors.shutdownGracefully();
    }

}
