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
 * CompletableFuture::anyOf()
 */
public class UsingAnyOf {
    private static final Logger log = LoggerFactory.getLogger(UsingAnyOf.class);

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

        CompletableFuture<Object> any = CompletableFuture.anyOf(cf1, cf2);
        while (!any.isDone()) {
            FakeTask.adder(10);
        }

        log.info("The first available result is {}", any.join());
    }
}
