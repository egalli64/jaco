/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s05;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CompletableFuture::allOf()
 */
public class UsingAllOf {
    private static final Logger log = LoggerFactory.getLogger(UsingAllOf.class);

    /**
     * Parallel execution of two CompletableFuture
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");
        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> Jobs.job(10));
        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(() -> Jobs.job(10));

        CompletableFuture.allOf(cf1, cf2).join();

        System.out.printf("Adding up the future results: %f%n", cf1.join() + cf1.join());
        log.trace("Exit");
    }
}
