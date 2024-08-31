/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s4;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * CompletableFuture::thenAccept()
 * <p>
 * The previous stage result is passed to a consumer, the resulting
 * CompletableFuture is Void
 */
public class ThenAccept {
    private static final Logger log = LoggerFactory.getLogger(ThenAccept.class);

    /**
     * Create a future, consumes to its result, do something else while the future
     * is not done
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");
        CompletableFuture<Void> cf = CompletableFuture.supplyAsync(() -> FakeTask.adder(2)) //
                .thenAccept(x -> log.info("Worker: {}, {}", x, FakeTask.adder(10)));

        log.info("Do something else until the future is not completed");
        while (!cf.isDone()) {
            FakeTask.adder(2);
        }

        log.info("Joining on a Void CompletableFuture gives ... {}", cf.join());
    }
}
