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
 * A simple common use for a CompletableFuture.
 * 
 * It is created in a (main) thread, another thread (worker) has to complete it.
 */
public class CompletableFuture2T {
    /**
     * Get a future, do some other job, then hang waiting for the future to complete
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        Future<Double> adder = futureCubeRootAdder(10);

        if (!adder.isDone()) {
            System.out.println("While the adder works, do something else in the main thread");
            System.out.println("Main thread result: " + aJob(10));
        } else {
            System.out.println("Unexpected, the adder should take some time to complete!");
        }

        System.out.println("When there is nothing more to do, wait the adder to complete");
        try {
            System.out.println("Worker result: " + adder.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create a completable future, run it on another thread.
     * 
     * The future is completed in that other thread.
     * 
     * @param count for the job performed by the completable future
     * @return the future locally created
     */
    private static Future<Double> futureCubeRootAdder(int count) {
        System.out.println("Create and start the completable future task");
        CompletableFuture<Double> future = new CompletableFuture<>();
        new Thread(() -> future.complete(aJob(count))).start();

        return future;
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
