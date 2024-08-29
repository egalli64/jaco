/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s8;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CompletableFuture introduction.
 * <p>
 * Even though it is possible to work on a completable future just in a single
 * thread, its common use is about simplifying communication between threads.
 */
public class CFutureBasicIntro {
    private static final Logger log = LoggerFactory.getLogger(CFutureBasicIntro.class);

    /**
     * Create a CompletableFuture and uses some of its most basic methods
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");
        CompletableFuture<String> cf = new CompletableFuture<>();

        if (!cf.isDone()) {
            System.out.println("cf is not done yet");
        } else {
            System.out.println("Unexpected!");
        }

        log.debug("Completing cf");
        cf.complete("Completed in the main thread");

        if (cf.isDone()) {
            System.out.println("cf has been completed");
        } else {
            System.out.println("Unexpected!");
        }

        try {
            System.out.println("cf value by get: " + cf.get());
        } catch (InterruptedException | ExecutionException e) {
            log.warn("You should not get here", e);
        }

        System.out.println("cf value by join: " + cf.join());
        log.trace("Exit");
    }
}
