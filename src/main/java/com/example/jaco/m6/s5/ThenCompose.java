/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s5;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * CompletableFuture::thenCompose()
 */
public class ThenCompose {
    private static final Logger log = LoggerFactory.getLogger(ThenCompose.class);

    /**
     * Compare use of thenApply and thenCompose when accepting functions that return
     * a CompletableFuture
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Supplier<Double> task1 = () -> FakeTasks.adder(10);
        Function<Double, CompletableFuture<Integer>> task2 = x -> CompletableFuture.completedFuture(x.intValue());

        // using thenApply results in nested CompletableFuture
        CompletableFuture<CompletableFuture<Integer>> applied = CompletableFuture.supplyAsync(task1).thenApply(task2);
        log.info("The result is {} (thenApply)", applied.join().join());

        // using thenCompose is more readable
        CompletableFuture<Integer> composed = CompletableFuture.supplyAsync(task1).thenCompose(task2);
        log.info("The result is {} (thenCompose)", composed.join());

        log.trace("Exit");
    }
}
