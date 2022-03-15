package com.janey668.c1;

public class SyncTest {
    static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i <= 5000; i++) {
                synchronized (SyncTest.class) {
                    count++;
                }

            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i <= 5000; i++) {
                synchronized (SyncTest.class) {
                    count--;
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count);
    }

}
