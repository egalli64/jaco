/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s7;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * CompletableFuture::handle()
 */
public class Handle {
    private static final Logger log = LoggerFactory.getLogger(Handle.class);

    /**
     * Create a CompletableFuture that randomly throws an exception. The handle step
     * converts the eventual exception into a fallback value or maps the received
     * result to another value.
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

        BiFunction<Double, Throwable, Double> handler = (result, ex) -> {
            if (ex != null) {
                log.warn("Fallback for {}", ex.getCause().toString());
                return 0.0;
            } else {
                log.info("Processing {} from previous step", result);
                return result * FakeTasks.adder(10);
            }
        };

        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(task).handle(handler);

        log.info("Result: {}", cf.join());
    }
}
