/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s05;

import java.util.concurrent.CompletableFuture;

/**
 * A completed future created by CompletableFuture::completedFuture()
 * 
 * No concurrency here. All the job is done in the main thread
 */
public class CompletableFuture2TCompleted {
    /**
     * Create the completable future, do some other job, then wait for the completable future
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        CompletableFuture<Double> cf = CompletableFuture.completedFuture(Jobs.job(10));

        if (cf.isDone()) {
            System.out.println("The future is done, still I have some job to do");
            System.out.printf("Main thread result: %f%n", Jobs.job(10));
        }

        System.out.printf("Completable future result: %f%n", cf.join());
    }
}
