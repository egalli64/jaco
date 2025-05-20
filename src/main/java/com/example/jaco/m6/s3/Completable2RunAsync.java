/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Simple creation of CompletableFuture by runAsync()
 */
public class Completable2RunAsync {
    private static final Logger log = LoggerFactory.getLogger(Completable2RunAsync.class);

    public static void main(String[] args) {
        log.trace("Enter");

        Future<Void> async = CompletableFuture.runAsync(() -> {
            log.debug("Enter");
            FakeTasks.adder(100_000);
        });

        while (!async.isDone()) {
            FakeTasks.adder(1_000);
        }

        log.trace("Exit");
    }
}
