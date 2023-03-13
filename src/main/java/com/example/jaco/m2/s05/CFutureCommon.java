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
 * A simple common use for a CompletableFuture.
 * 
 * It is created in a (main) thread, another thread (worker) completes it.
 */
public class CFutureCommon {
    private static final Logger log = LoggerFactory.getLogger(CFutureCommon.class);

    /**
     * Get a completable future, do some other job, then hang waiting for the future to complete
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<Double> cf = new CompletableFuture<>();
        new Thread(() -> cf.complete(Jobs.job(10)), "CF").start();

        if (!cf.isDone()) {
            log.trace("The future job is not done, do something else");
            System.out.printf("Main thread result: %f%n", Jobs.job(3));
        }

        log.trace("Finally join on the future");
        System.out.printf("Worker result: %f%n", cf.join());
        log.trace("Exit");
    }
}
