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
 * CompletableFuture::thenAccept()
 * <p>
 * The previous stage result is passed to a consumer, the resulting Future
 * contains a Void
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
        CompletableFuture<Void> cf = CompletableFuture.supplyAsync(() -> FakeTask.adder(10)) //
                .thenAccept(x -> System.out.println("Worker: " + x + ", " + FakeTask.adder(10)));

        log.trace("Do something else until the future is not completed");
        while (!cf.isDone()) {
            System.out.println("Main: " + FakeTask.adder(2));
        }

        System.out.println("Joining on a Void CompletableFuture gives ... " + cf.join());
        log.trace("Exit");
    }
}
