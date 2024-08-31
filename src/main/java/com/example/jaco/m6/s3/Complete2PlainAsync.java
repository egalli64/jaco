/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s3;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Asynchronous completion by complete()
 * <p>
 * It is created in a (main) thread, another thread (worker) completes it.
 */
public class Complete2PlainAsync {
    private static final Logger log = LoggerFactory.getLogger(Complete2PlainAsync.class);

    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<Double> cf = new CompletableFuture<>();
        log.debug("Created {}", cf);

        new Thread(() -> cf.complete(FakeTask.adder(1_000)), "worker").start();

        while (!cf.isDone()) {
            FakeTask.adder(3);
        }

        log.info("Join: {}", cf.join());
    }
}
