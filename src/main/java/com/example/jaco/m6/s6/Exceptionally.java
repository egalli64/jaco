/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s6;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * CompletableFuture::exceptionally()
 */
public class Exceptionally {
    private static final Logger log = LoggerFactory.getLogger(Exceptionally.class);

    /**
     * Create a CompletableFuture that randomly could throw an exception. The
     * exceptionally step convert the eventual exception in a fallback value.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            if (ThreadLocalRandom.current().nextBoolean()) {
                throw new IllegalStateException("Something went wrong!");
            }
            return FakeTask.adder(10);
        }).exceptionally(ex -> {
            log.warn("Here is the exception", ex);
            return 0.0;
        });

        log.info("Join: {}", cf.join());
    }
}
