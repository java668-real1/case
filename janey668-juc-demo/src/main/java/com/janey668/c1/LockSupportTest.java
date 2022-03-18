package com.janey668.c1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.sleep;

@Slf4j
public class LockSupportTest {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.info("1");
        });


        Thread t2 = new Thread(() -> {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.info("2");
            LockSupport.unpark(t1);
        });
        t1.start();
        t2.start();


    }

}
