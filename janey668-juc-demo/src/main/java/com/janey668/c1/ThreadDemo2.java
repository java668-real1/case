package com.janey668.c1;

/**
 * 实现 Runnable 接口创建线程
 */
public class ThreadDemo2 {

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("t1 线程运行了");
            }
        };
        Thread t1 = new Thread(runnable, "t1");
        t1.start();
        System.out.println("main 线程运行了");
    }


}
