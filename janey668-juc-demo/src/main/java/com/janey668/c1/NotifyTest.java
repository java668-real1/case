package com.janey668.c1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotifyTest {
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    log.info("t1111");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    log.info("t2222");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(1000);
        synchronized (lock) {
            lock.notifyAll();
        }

    }

}