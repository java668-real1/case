package com.janey668.c1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test1 {
    static boolean b = true;

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            while (b) {
                log.info("Test1");
            }
        }).start();
        Thread.sleep(1000);
        b = false;
    }


}
