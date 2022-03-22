package com.janey668;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

@Slf4j
public class TestReentrantLock {

    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        new Thread(() -> {
            lock.lock();
            try {
                sleep(1000);
                log.info("Thread1 runing...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            lock.lock();
            try {
                sleep(2000);
                log.info("Thread2 runing...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            lock.lock();
            try {
                log.info("Thread3 runing...");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();
    }

}
