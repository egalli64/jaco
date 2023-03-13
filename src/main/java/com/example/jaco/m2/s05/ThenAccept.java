/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s05;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CompletableFuture::thenAccept()
 * 
 * The previous stage result is passed to a consumer, the resulting Future contains a Void
 */
public class ThenAccept {
    private static final Logger log = LoggerFactory.getLogger(ThenAccept.class);

    /**
     * Create a future, consumes to its result, do something else while the future is not done
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");
        CompletableFuture<Void> cf = CompletableFuture.supplyAsync(() -> Jobs.job(10)) //
                .thenAccept(x -> System.out.printf("Worker: %f%n", x));

        log.trace("Do something else until the future is not completed");
        while (!cf.isDone()) {
            System.out.printf("Main: %f%n", Jobs.job(10));
        }

        System.out.println("Joining on a Void CompletableFuture gives ... " + cf.join());
        log.trace("Exit");
    }
}
