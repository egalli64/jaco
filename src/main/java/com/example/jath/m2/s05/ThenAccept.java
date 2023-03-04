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
 * CompletableFuture::thenAccept()
 * 
 * The previous stage result is passed to a consumer, the resulting Future contains a Void
 */
public class ThenAccept {
    /**
     * Create a future, consumes to its result, do something else while the future is not done
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("Create and start the completable future task");
        Future<Void> adder = CompletableFuture.supplyAsync(() -> aJob(10)) //
                .thenAccept(x -> System.out.println("Worker result: " + x));

        while (!adder.isDone()) {
            System.out.println("The adder works, the main thread does something else: " + aJob(3));
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
