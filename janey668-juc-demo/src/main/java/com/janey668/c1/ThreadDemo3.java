package com.janey668.c1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * callable FutureTask 创建线程
 */
public class ThreadDemo3 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<Integer>(() -> {
            System.out.println("running");
            TimeUnit.SECONDS.sleep(2);
            return 300;
        });
        Thread t1 = new Thread(futureTask, "t1");
        t1.start();
        Integer result = futureTask.get();
        System.out.println(result);
    }

}
