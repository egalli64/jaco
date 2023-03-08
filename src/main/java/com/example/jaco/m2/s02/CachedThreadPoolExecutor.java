/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s02;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.DoubleStream;

/**
 * Executor, ThreadPoolExecutor via Executors::newCachedThreadPool()
 */
public class CachedThreadPoolExecutor {
    /**
     * The Cached Thread Pool Executor starts with a core pool sized zero. Sending two batches of three
     * tasks, we see only three threads also in the second batch. The third batch has five tasks, so
     * more threads are created.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("-Cached Thread Pool on runnables-");

        ExecutorService executor = Executors.newCachedThreadPool();

        // first batch
        for (int i = 0; i < 3; i++) {
            executor.execute(new Hello());
        }

        double x = DoubleStream.generate(Math::random).limit(1_000_000).map(Math::cbrt).sum();
        System.out.println("Doing something else in main: " + x);
        System.out.println("Then assigning other tasks to the executor");

        // second batch
        for (int i = 0; i < 3; i++) {
            executor.execute(new Hello());
        }

        x = DoubleStream.generate(Math::random).limit(100).map(Math::cbrt).sum();
        System.out.println("Doing something else in main: " + x);
        System.out.println("Then assigning other tasks to the executor");

        // third batch, requires more threads
        for (int i = 0; i < 5; i++) {
            executor.execute(new Hello());
        }

        executor.shutdown();
    }
}
