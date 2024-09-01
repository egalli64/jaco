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
 * CompletableFuture::handle()
 */
public class Handle {
    private static final Logger log = LoggerFactory.getLogger(Handle.class);

    /**
     * Create a CompletableFuture that randomly could throw an exception. The handle
     * step convert the eventual exception in a fallback value or map the received
     * result to another value
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> {
            if (ThreadLocalRandom.current().nextBoolean()) {
                throw new RuntimeException("Something went wrong!");
            }
            return FakeTask.adder(10);
        }).handle((result, ex) -> {
            if (ex != null) {
                log.warn("Exception occurred", ex);
                return 0.0;
            } else {
                return result * FakeTask.adder(10);
            }
        });

        log.info("Join: {}", cf.join());
    }
}
