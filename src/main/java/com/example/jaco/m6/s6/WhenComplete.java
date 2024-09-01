/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s6;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * CompletableFuture::whenComplete()
 */
public class WhenComplete {
    private static final Logger log = LoggerFactory.getLogger(WhenComplete.class);

    /**
     * Create a CompletableFuture that randomly could throw an exception. The
     * whenComplete step logs the exception or the result, but do not interfere with
     * the completable future.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            if (ThreadLocalRandom.current().nextBoolean()) {
                throw new IllegalStateException("Something went wrong!");
            }
            return FakeTask.adder(10);
        }).whenComplete((result, ex) -> {
            if (ex != null) {
                log.warn("Exception occurred", ex);
            } else {
                log.info("Result is {}", result);
            }
        });

        try {
            log.info("Join: {}", cf.join());
        } catch (Exception ex) {
            log.warn("There is a problem, {}", ex.getMessage());
        }
    }
}
