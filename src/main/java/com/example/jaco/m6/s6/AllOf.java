/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s6;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * CompletableFuture::allOf()
 */
public class AllOf {
    private static final Logger log = LoggerFactory.getLogger(AllOf.class);

    /**
     * Create a few asynchronous CompletableFutures, create a new CompletableFuture
     * that completes when all of them are completed. Do something else in main
     * until it is done. Join the original ones.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Supplier<Double> task = () -> FakeTask.adder(10);

        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(task);
        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(task);
        CompletableFuture<Double> cf3 = CompletableFuture.supplyAsync(task);

        CompletableFuture<Void> all = CompletableFuture.allOf(cf1, cf2, cf3);
        while (!all.isDone()) {
            FakeTask.adder(1);
        }

        log.info("Future results: {}, {}, {}", cf1.join(), cf2.join(), cf3.join());
    }
}
