/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s6;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * CompletableFuture::thenCombine()
 */
public class ThenCombine {
    private static final Logger log = LoggerFactory.getLogger(ThenCombine.class);

    /**
     * Create two asynchronous CompletableFutures, combine them. Do something else
     * in main until they are done. Join on the combined result.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Supplier<Double> task = () -> FakeTasks.adder(10);

        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(task);
        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(task);

        BiFunction<Double, Double, Double> combiner = (left, right) -> {
            log.trace("Combining results: {} and {}", left, right);
            return left + right;
        };

        CompletableFuture<Double> combined = cf1.thenCombine(cf2, combiner);

        while (!combined.isDone()) {
            FakeTasks.adder(1);
        }

        log.info("Combined result: {}", combined.join());
    }
}
