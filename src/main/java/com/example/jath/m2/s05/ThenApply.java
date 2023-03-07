/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s05;

import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture::thenApply()
 * 
 * The previous stage result is passed to a function return another CompletableFuture
 */
public class ThenApply {
    /**
     * Create a future, apply a function to its result, do something else then use the future result
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("Create and start a completable future task");
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> Jobs.job(10)) //
                .thenApply(x -> String.format("Worker result: %f", x));

        if (!cf.isDone()) {
            System.out.println("The future job is not done, do something else in the main thread");
            System.out.printf("Main thread result: %f%n", Jobs.job(10));
        }

        System.out.println("When there is nothing more to do, wait the future to complete");
        System.out.println(cf.join());
    }
}
