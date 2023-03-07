/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s05;

import java.util.concurrent.CompletableFuture;

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
        CompletableFuture<Void> cf = CompletableFuture.supplyAsync(() -> Jobs.job(10)) //
                .thenAccept(x -> System.out.printf("Worker result: %f%n", x));

        System.out.println("While the future job is not done, do something else in the main thread");
        while (!cf.isDone()) {
            System.out.printf("Main thread result: %f%n", Jobs.job(10));
        }

        System.out.println("Joining on a Void CompletableFuture gives ... " + cf.join());
    }
}
