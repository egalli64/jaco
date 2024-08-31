/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s4;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * CompletableFuture::thenApply()
 * <p>
 * The previous stage result is passed to a function that maps it to a new value
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
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> FakeTask.adder(2)) //
                .thenApply(x -> x + ", " + FakeTask.adder(10));

        log.info("Do something else until the future is not completed");
        while (!cf.isDone()) {
            FakeTask.adder(2);
        }

        log.info("Join gives: {}", cf.join());
    }
}
