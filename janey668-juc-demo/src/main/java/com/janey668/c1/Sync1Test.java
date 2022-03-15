package com.janey668.c1;

public class Sync1Test {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i <= 5000; i++) {
                Work.incr();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i <= 5000; i++) {
                Work.decr();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(Work.count);
    }

}

class Work{

    static int count = 0;

    public static synchronized void incr() {
        count ++;
    }


    public static synchronized void decr() {
        count --;
    }
}
