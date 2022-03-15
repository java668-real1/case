package com.janey668.c1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j
public class ParkTest {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                log.info("park");
                LockSupport.park();
                log.info("unpark 打断状态 {}", Thread.currentThread().isInterrupted());
            }
        });
        t1.start();
        Thread.sleep(100);
        t1.interrupt();

    }
}
