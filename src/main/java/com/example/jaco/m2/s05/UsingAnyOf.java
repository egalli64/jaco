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
        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> Jobs.job(10));
        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(() -> Jobs.job(10));

        CompletableFuture<Object> completed = CompletableFuture.anyOf(cf1, cf2);
        System.out.printf("A result is %f%n", completed.join());
        log.trace("Exit");
    }
}
