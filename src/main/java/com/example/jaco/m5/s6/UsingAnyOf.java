/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s6;

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
     * Parallel execution of two CompletableFuture
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");
        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> FakeTask.adder(10));
        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(() -> FakeTask.adder(10));

        CompletableFuture<Object> completed = CompletableFuture.anyOf(cf1, cf2);
        System.out.println("The first available result is " + completed.join());
        log.trace("Exit");
    }
}
