/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s05;

import java.util.concurrent.CompletableFuture;

/**
 * A simple common use for a CompletableFuture.
 * 
 * It is created in a (main) thread, another thread (worker) completes it.
 */
public class CompletableFuture2T {
    /**
     * Get a completable future, do some other job, then hang waiting for the future to complete
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        CompletableFuture<Double> cf = Jobs.futureJob(10);

        if (!cf.isDone()) {
            System.out.println("The future job is not done, do something else in the main thread");
            System.out.printf("Main thread result: %f%n", Jobs.job(3));
        }

        System.out.println("When there is nothing more to do, wait the future to complete");
        System.out.printf("Worker result: %f%n", cf.join());
    }
}
