package com.janey668.c1;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BalkingTest {
    // 用来表示是否已经有线程已经在执行启动了
    private volatile boolean starting;

    public static void main(String[] args) {
        BalkingTest balkingTest = new BalkingTest();
        List<Thread> list = new ArrayList(5);
        for (int i = 0; i < 5; i++) {
            list.add(new Thread(() -> {
                balkingTest.start();
            }));
        }
        list.forEach(Thread::start);

    }


    public void start() {
        log.info("尝试启动监控线程...");
        synchronized (this) {
            if (starting) {
                log.info("重复启动...");
                return;
            }
            starting = true;
            log.info("starting...");
        }

        // 真正启动监控线程...
    }

}

