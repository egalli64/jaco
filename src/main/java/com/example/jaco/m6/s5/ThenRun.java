/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s5;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * CompletableFuture::thenRun()
 * <p>
 * When the previous stage is done, a runnable is run
 */
public class ThenRun {
    private static final Logger log = LoggerFactory.getLogger(ThenRun.class);

    /**
     * Create a completable future, print a message when done, do something else in
     * the meantime
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Supplier<Double> task1 = () -> FakeTask.adder(1_000);
        Runnable task2 = () -> log.trace("In the runner");

        CompletableFuture<Void> cf = CompletableFuture.supplyAsync(task1).thenRun(task2);

        log.info("Do something else until the future is not completed");
        while (!cf.isDone()) {
            FakeTask.adder(10);
        }

        log.info("Join on the (void) future: {}", cf.join());
    }
}
