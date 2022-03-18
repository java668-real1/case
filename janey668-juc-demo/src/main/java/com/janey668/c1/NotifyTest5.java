package com.janey668.c1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotifyTest5 {

    static Object lock = new Object();
    static volatile boolean bIsRun = false;

    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            synchronized (lock) {
                if (!bIsRun) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("a");
            }

        });

        Thread b = new Thread(() -> {
            synchronized (lock) {
                System.out.println("b");
                bIsRun = true;
                lock.notifyAll();
            }
        });

        a.start();
        b.start();
    }

}
