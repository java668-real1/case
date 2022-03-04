package com.janey668.c1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 继承 Thread 类创建线程
 */
public class ThreadDemo1 {
    static final Logger logger = LoggerFactory.getLogger(ThreadDemo1.class);
    public static void main(String[] args) {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                logger.info("t1 线程运行");
            }
        };
        t1.setName("t1");
        t1.start();
        logger.info("main 线程运行");
    }

    private String name;


}
