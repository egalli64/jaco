/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s05;

import java.util.concurrent.CompletableFuture;
import java.util.stream.DoubleStream;

/**
 * Utility class for job simulations
 */
public class Jobs {
    /**
     * A simple job
     * 
     * @param size number of items used to generate the result
     * @return a double
     */
    public static double job(int size) {
        double result = DoubleStream.generate(() -> Math.random()).limit(size).sum();
        System.out.printf("%s: %f%n", Thread.currentThread().getName(), result);
        return result;
    }

    /**
     * Create a completable future in the current thread, run and complete it in another thread.
     * 
     * @param size for the job performed by the completable future
     * @return the created completable future
     */
    public static CompletableFuture<Double> futureJob(int size) {
        System.out.println("Create and start a completable future");
        CompletableFuture<Double> result = new CompletableFuture<>();
        new Thread(() -> result.complete(job(size))).start();

        return result;
    }
}
