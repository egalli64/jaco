package com.example.jath.m2.s06;

import java.util.concurrent.CountDownLatch;

public class Latch {
    private static final int NUMBER_OF_THREADS = 2;

    public static void main(String[] args) {
        CountDownLatch cdl = new CountDownLatch(NUMBER_OF_THREADS);

        Runnable worker = () -> {
            String name = Thread.currentThread().getName();

            try {
                System.out.println(name + " ...");
                Thread.sleep((long) (Math.random() * 1_000));
                System.out.println(name + " done [" + cdl.getCount() + "]");
                cdl.countDown();
            } catch (InterruptedException e) {
                System.out.println("No exception expected here");
                throw new IllegalStateException(e);
            }
        };

        Thread[] ts = { new Thread(worker), new Thread(worker), new Thread(worker), new Thread(worker) };

        for (Thread t : ts) {
            t.start();
        }

        try {
            cdl.await();
            System.out.println("Go!");
        } catch (InterruptedException e) {
            System.out.println("No exception expected here");
            throw new IllegalStateException(e);
        }
    }
}
