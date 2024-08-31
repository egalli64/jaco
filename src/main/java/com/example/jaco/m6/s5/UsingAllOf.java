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
 * CompletableFuture::allOf()
 */
public class UsingAllOf {
    private static final Logger log = LoggerFactory.getLogger(UsingAllOf.class);

    /**
     * Create two asynchronous completable futures, create a new completable future
     * that completes when all of them are completed. Do something else in main
     * until it is done. Join the original ones.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");
        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> FakeTask.adder(10));
        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(() -> FakeTask.adder(10));

        CompletableFuture<Void> all = CompletableFuture.allOf(cf1, cf2);
        while (!all.isDone()) {
            FakeTask.adder(10);
        }

        log.info("All results are: {}, {}", cf1.join(), cf2.join());
    }
}
