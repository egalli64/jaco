/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s5;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * CompletableFuture::thenCombine()
 */
public class ThenCombine {
    private static final Logger log = LoggerFactory.getLogger(ThenCombine.class);

    /**
     * Create two asynchronous completable futures, combine them. Do something else
     * in main until they are done. Join on the combination.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> FakeTask.adder(10));
        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(() -> FakeTask.adder(10));
        log.debug("cf1 done is {}, cf2 done is {}", cf1.isDone(), cf2.isDone());

        CompletableFuture<Double> combined = cf1.thenCombine(cf2, (a, b) -> a + b);

        while (!combined.isDone()) {
            FakeTask.adder(10);
        }

        log.info("Join: {}", combined.join());
    }
}
