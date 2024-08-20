/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s5;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple common use for a CompletableFuture.
 * <p>
 * It is created in a (main) thread, another thread (worker) completes it.
 */
public class CFutureCommon {
    private static final Logger log = LoggerFactory.getLogger(CFutureCommon.class);

    /**
     * Get a completable future, do some other job, then wait the future to complete
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<Double> cf = new CompletableFuture<>();
        new Thread(() -> cf.complete(Jobs.job(10)), "CF").start();

        if (!cf.isDone()) {
            log.trace("If the future job is not done, do something else");
            System.out.printf("Main thread result: %f%n", Jobs.job(3));
        }

        log.trace("Then join on the future");
        Double result = cf.join();

        System.out.printf("Worker result: %f%n", result);
        log.trace("Exit");
    }
}
