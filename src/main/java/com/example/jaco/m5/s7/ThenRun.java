/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s7;

import java.util.concurrent.CompletableFuture;

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
        CompletableFuture<Void> cf = CompletableFuture.supplyAsync(() -> FakeTask.adder(100)) //
                .thenRun(() -> log.trace("In the runner"));

        log.trace("Do something else until the future is not completed");
        while (!cf.isDone()) {
            System.out.printf("Main thread result: %f\n", FakeTask.adder(10));
        }

        System.out.println("The CompletableFuture returned by thenRun() is Void: " + cf.join());
        log.trace("Exit");
    }
}
