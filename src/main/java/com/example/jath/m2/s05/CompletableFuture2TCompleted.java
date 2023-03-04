/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s05;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.DoubleStream;

/**
 * A completed future created by CompletableFuture::completedFuture()
 */
public class CompletableFuture2TCompleted {
    /**
     * Get a future, do some other job, then hang waiting for the future to complete
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        Future<Double> adder = futureCubeRootAdder(10);

        if (adder.isDone()) {
            System.out.println("The future returned is already completed!");
            System.out.println("Main thread result: " + aJob(10));
        }

        try {
            System.out.println("Worker result: " + adder.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create a completed completable future, the job is done in the current thread.
     * 
     * @param count for the job performed locally
     * @return the already completed future
     */
    private static Future<Double> futureCubeRootAdder(int count) {
        System.out.println("Create and start the completable future task");
        double result = aJob(count);
        return CompletableFuture.completedFuture(result);
    }

    /**
     * A simple job that takes some time
     * 
     * @param size size of the job
     * @return a double
     */
    private static double aJob(int size) {
        return DoubleStream.generate(() -> Math.cbrt(Math.random())).limit(size).sum();
    }
}
