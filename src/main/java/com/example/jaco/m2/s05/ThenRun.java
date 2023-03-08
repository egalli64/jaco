/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s05;

import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture::thenRun()
 * 
 * When the previous stage is done, a runnable is run
 */
public class ThenRun {
    /**
     * Create a completable future, print a message when done, do something else in the meantime
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("Create and start the completable future task");
        CompletableFuture<Void> cf = CompletableFuture.supplyAsync(() -> Jobs.job(5_000)) //
                .thenRun(() -> System.out.println("Worker is done"));

        System.out.println("While the future job is not done, do something else in the main thread");
        while (!cf.isDone()) {
            System.out.printf("Main thread result: %f%n", Jobs.job(10));
        }

        System.out.println("The CompletableFuture returned by thenRun() is Void: " + cf.join());
    }
}
