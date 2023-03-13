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
 * CompletableFuture::thenRun()
 * 
 * When the previous stage is done, a runnable is run
 */
public class ThenRun {
    private static final Logger log = LoggerFactory.getLogger(ThenRun.class);

    /**
     * Create a completable future, print a message when done, do something else in the meantime
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");
        CompletableFuture<Void> cf = CompletableFuture.supplyAsync(() -> Jobs.job(5_000)) //
                .thenRun(() -> log.trace("In the runner"));

        log.trace("Do something else until the future is not completed");
        while (!cf.isDone()) {
            System.out.printf("Main thread result: %f%n", Jobs.job(10));
        }

        System.out.println("The CompletableFuture returned by thenRun() is Void: " + cf.join());
        log.trace("Exit");
    }
}
