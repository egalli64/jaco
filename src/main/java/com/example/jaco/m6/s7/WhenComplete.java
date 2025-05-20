/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s7;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * CompletableFuture::whenComplete()
 */
public class WhenComplete {
    private static final Logger log = LoggerFactory.getLogger(WhenComplete.class);

    /**
     * Create a CompletableFuture that randomly throws an exception. The
     * whenComplete step logs the exception or the result, but does not interfere
     * with the CompletableFuture chain.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Supplier<Double> task = () -> {
            if (ThreadLocalRandom.current().nextBoolean()) {
                log.info("Generating an exception");
                throw new IllegalStateException("Something went wrong!");
            } else {
                log.info("No exception, proceeding with task");
                return FakeTasks.adder(10);
            }
        };

        BiConsumer<Double, Throwable> handler = (result, ex) -> {
            if (ex != null) {
                log.warn("The exception is {}", ex.getCause().toString());
            } else {
                log.info("The result is {}", result);
            }
        };

        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(task).whenComplete(handler);

        try {
            log.info("The chain result is {}", cf.join());
        } catch (Exception ex) {
            log.warn("The chain threw {}", ex.getMessage());
        }
    }
}
