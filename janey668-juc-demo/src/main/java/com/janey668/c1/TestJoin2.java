package com.janey668.c1;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j
public class TestJoin2 {

    static int r = 0;
    static int r1 = 0;
    static boolean b = false;
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
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (b) {
                log.debug("=======");
            }
            log.debug("t2结束:{}", b);
            r1 = 10;
        });
        t1.start();
        t2.start();
        b = true;
        t1.join();
        t2.join();
        log.debug("结果为:{}", r);
        log.debug("结果为:{}", r1);
        log.debug("结束");
    }


//    static int r1 = 0;
//    static int r2 = 0;
//
//    public static void main(String[] args) throws InterruptedException {
//        test2();
//    }

//    private static void test2() throws InterruptedException {
//        Thread t1 = new Thread(() -> {
//            try {
//                sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            r1 = 10;
//        });
//        Thread t2 = new Thread(() -> {
//            try {
//                sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            r2 = 20;
//
//
//        });
//        long start = System.currentTimeMillis();
//        t1.start();
//        t2.start();
//        t1.join();
//        t2.join();
//        long end = System.currentTimeMillis();
//        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
//    }
}