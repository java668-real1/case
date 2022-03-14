package com.janey668.c1;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j
public class TestJoin3 {

    static int r = 0;
    static int r1 = 0;
    public static void main(String[] args) throws InterruptedException {
        test1();
    }
    private static void test1() throws InterruptedException {
        log.debug("开始");
        Thread t1 = new Thread(() -> {
            log.debug("开始");
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("结束");
            r = 10;
        });
        Thread t2 = new Thread(() -> {
            log.debug("t2开始");
            try {
                sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t2结束");
            r1 = 10;
        });
        t1.start();
        t2.start();
        t1.join(3000);
        t2.join(4000);
        log.debug("结果为:{}", r);
        log.debug("结果为:{}", r1);
        log.debug("结束");
    }

}
