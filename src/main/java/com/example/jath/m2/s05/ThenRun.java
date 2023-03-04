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
 * CompletableFuture::thenRun()
 * 
 * When the previous stage is done, a runnable is run, the resulting Future contains a Void
 */
public class ThenRun {
    /**
     * Create a future, print a message when done, do something else in the meantime
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("Create and start the completable future task");
        Future<Void> adder = CompletableFuture.supplyAsync(() -> aJob(5_000)) //
                .thenRun(() -> System.out.println("Worker is done"));

        while (!adder.isDone()) {
            System.out.println("The adder works, the main thread does something else: " + aJob(10));
        }

        try {
            System.out.println("Getting a Void is not useful: " + adder.get());
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
