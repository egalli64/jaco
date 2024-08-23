/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s5;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

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
        new Thread(() -> cf.complete(FakeTask.adder(10)), "worker").start();

        if (!cf.isDone()) {
            log.trace("The future is not done, do something else in the main thread");
            System.out.println("Main thread result: " + FakeTask.adder(3));
        } else {
            log.trace("The future has been already completed!");
        }

        log.trace("Join on the future");
        Double result = cf.join();

        System.out.println("Worker result: " + result);
        log.trace("Exit");
    }
}
