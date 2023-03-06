/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s08;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Stream;

/**
 * CountDownLatch
 */
public class Latch extends ProblemFrame {
    /**
     * Create a latch set to TASK_NR, run TASK_NR tasks, each task decrease the latch count.
     * 
     * Main wait on latch, when the count down is done, gather the result from the tasks and print it.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        DoubleAdder accumulator = new DoubleAdder();
        CountDownLatch cdl = new CountDownLatch(TASK_NR);

        Runnable worker = () -> {
            String name = Thread.currentThread().getName();
            double value = job(100);
            System.out.printf("%s: %f%n", name, value);
            accumulator.add(value);
            System.out.println(name + " done [" + cdl.getCount() + "]");
            cdl.countDown();
        };

        Stream.generate(() -> new Thread(worker)).limit(TASK_NR).forEach(t -> t.start());

        try {
            System.out.println("Main waits on latch: " + cdl.getCount());
            cdl.await();
            System.out.println("Main sees countdown completed: " + cdl.getCount());
        } catch (InterruptedException e) {
            System.out.println("No exception expected here");
            throw new IllegalStateException(e);
        }
        System.out.printf("Result: %f%n", accumulator.sum());
    }
}
