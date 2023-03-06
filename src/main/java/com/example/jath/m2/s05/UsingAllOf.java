/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s05;

import java.util.concurrent.CompletableFuture;
import java.util.stream.DoubleStream;

/**
 * CompletableFuture::allOf() - A CompletableFuture union of all its arguments
 * 
 * CompletableFuture::join() - hangs for completion and return the calculated value
 */
public class UsingAllOf {
    /**
     * Parallel execution of two CompletableFuture
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> aJob(10));
        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(() -> aJob(10));

        CompletableFuture.allOf(cf1, cf2).join();

        System.out.println(cf1.join() + cf1.join());
    }

    /**
     * A simple job that takes some time
     * 
     * @param size size of the job
     * @return a double
     */
    private static double aJob(int size) {
        double result = DoubleStream.generate(() -> Math.random()).limit(size).sum();
        System.out.printf("%s: %f%n", Thread.currentThread().getName(), result);
        return result;
    }
}
