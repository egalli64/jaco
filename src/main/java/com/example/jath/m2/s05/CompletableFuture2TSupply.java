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
 * A CompletableFuture created by supplyAsync()
 */
public class CompletableFuture2TSupply {
    public static void main(String[] args) {
        System.out.println("Create and start the completable future task");
        Future<Double> adder = CompletableFuture.supplyAsync(() -> aJob(10));

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
     * A simple job that takes some time
     * 
     * @param size size of the job
     * @return a double
     */
    private static double aJob(int size) {
        return DoubleStream.generate(() -> Math.cbrt(Math.random())).limit(size).sum();
    }
}
