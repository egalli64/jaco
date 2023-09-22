/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s05;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CompletableFuture::thenApply()
 * <p>
 * The previous stage result is passed to a function return another
 * CompletableFuture
 */
public class ThenApply {
    private static final Logger log = LoggerFactory.getLogger(ThenApply.class);

    /**
     * Create a future, apply a function to its result, do something else then use
     * the future result
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> Jobs.job(10)) //
                .thenApply(x -> String.format("Worker: %f", x));

        log.trace("Do something else until the future is not completed");
        while (!cf.isDone()) {
            System.out.printf("Main: %f%n", Jobs.job(10));
        }

        log.trace("Then join on the future");
        System.out.println(cf.join());
        log.trace("Exit");
    }
}
