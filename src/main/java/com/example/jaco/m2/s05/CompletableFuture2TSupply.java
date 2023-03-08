/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s05;

import java.util.concurrent.CompletableFuture;

/**
 * A CompletableFuture created by supplyAsync()
 */
public class CompletableFuture2TSupply {
    public static void main(String[] args) {
        System.out.println("Create and start the completable future task");
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> Jobs.job(10));

        if (!cf.isDone()) {
            System.out.println("The future job is not done, do something else in the main thread");
            System.out.printf("Main thread result: %f%n", Jobs.job(10));
        }

        System.out.println("When there is nothing more to do, wait the future to complete");
        System.out.println("Worker result: " + cf.join());
    }
}
