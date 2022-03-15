package com.janey668.c1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InterruptTest {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while(true) {
                Thread current = Thread.currentThread();
                boolean interrupted = current.isInterrupted();
                log.debug(" 打断状态: {}", interrupted);
                if(interrupted) {
                    log.debug(" 打断状态: {}", interrupted);
                    break;
                }

            }
        });
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}
