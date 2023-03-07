/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s05;

import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture::anyOf()
 */
public class UsingAnyOf {
    /**
     * Parallel execution of two CompletableFuture
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(() -> Jobs.job(10));
        CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(() -> Jobs.job(10));

        CompletableFuture<Object> completed = CompletableFuture.anyOf(cf1, cf2);
        System.out.printf("The fastest task result is %f%n", completed.join());
    }
}
