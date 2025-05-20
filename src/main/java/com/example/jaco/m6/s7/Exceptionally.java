/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s7;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * CompletableFuture::exceptionally()
 */
public class Exceptionally {
    private static final Logger log = LoggerFactory.getLogger(Exceptionally.class);

    /**
     * Create a CompletableFuture that randomly throws an exception. The
     * exceptionally step convert the eventual exception in a fallback value.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Supplier<Double> task = () -> {
            if (ThreadLocalRandom.current().nextBoolean()) {
                log.trace("Generating an exception");
                throw new IllegalStateException("Something went wrong!");
            } else {
                log.trace("No exception, proceeding with task");
                return FakeTasks.adder(10);
            }
        };

        Function<Throwable, Double> fallback = ex -> {
            log.warn("Fallback for {}", ex.getCause().toString());
            return 0.0;
        };

        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(task).exceptionally(fallback);

        log.info("Result: {}", cf.join());
    }
}
