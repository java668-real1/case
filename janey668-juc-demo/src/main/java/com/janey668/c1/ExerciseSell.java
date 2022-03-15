package com.janey668.c1;

import java.util.*;

public class ExerciseSell {

    public static void main(String[] args) throws Exception {
        Ticket ticket = new Ticket(4500);
        List<Thread> threadList = new ArrayList<>(1000);
//        List<Integer> sellTotal = new ArrayList<>(1000);
        List<Integer> sellTotal = Collections.synchronizedList(new ArrayList<>(1000));
        for (int i = 0; i < 2000; i++){
            threadList.add(new Thread(() -> {
                sellTotal.add(ticket.sell(getSellNum()));
            }));
        }
        threadList.forEach(Thread::start);
        threadList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(sellTotal.stream().mapToInt(c -> c).sum());

    }

    static Random random = new Random();

    public static int getSellNum() {
        return random.nextInt(5) + 1;
    }

}

class Ticket {
    private int count;

    public Ticket(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public int sell(int amount) {
        if (this.count >= amount) {
            synchronized (this) {
                this.count -= amount;
            }
            return amount;
        } else {
            return 0;
        }
    }


}