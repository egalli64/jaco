/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s5;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * CompletableFuture::thenAccept()
 * <p>
 * The previous stage result is passed to a consumer, the resulting
 * CompletableFuture is Void
 */
public class ThenAccept {
    private static final Logger log = LoggerFactory.getLogger(ThenAccept.class);

    /**
     * Create a future, consume to its result, do something else while the future is
     * not done
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Supplier<Double> task1 = () -> FakeTasks.adder(2);

        Consumer<Double> task2 = x -> {
            log.info("Task 2 received {} from task 1", x);
            double processedValue = x * FakeTasks.adder(10);
            log.info("Task 2 uses it input to generate {}", processedValue);
        };

        CompletableFuture<Void> cf = CompletableFuture.supplyAsync(task1).thenAccept(task2);

        log.info("Do something else until the future is not completed");
        while (!cf.isDone()) {
            FakeTasks.adder(2);
        }

        log.info("Joining on a Void CompletableFuture gives ... {}", cf.join()); // null
    }
}
