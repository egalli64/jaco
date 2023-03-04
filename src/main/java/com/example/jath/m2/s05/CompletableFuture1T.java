/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s05;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletableFuture introduction.
 * 
 * Even though it is possible to work on a completable future just in a single thread, its common
 * use is about simplifying communication between threads.
 */
public class CompletableFuture1T {
    /**
     * Create a CompletableFuture and uses some of its most basic methods
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        CompletableFuture<String> cf = new CompletableFuture<>();

        if (!cf.isDone()) {
            System.out.println("cf has to be completed");
        }

        cf.complete("Completed in the main thread");

        if (cf.isDone()) {
            System.out.println("Now cf has been completed");
        }

        try {
            System.out.println("cf value is: " + cf.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
        }
    }
}
