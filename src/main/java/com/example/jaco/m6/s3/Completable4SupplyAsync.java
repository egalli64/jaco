/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Simple creation of CompletableFuture by supplyAsinc()
 */
public class Completable4SupplyAsync {
    private static final Logger log = LoggerFactory.getLogger(Completable4SupplyAsync.class);

    public static void main(String[] args) {
        log.trace("Enter");

        Future<Double> async = CompletableFuture.supplyAsync(() -> {
            log.debug("Enter");
            return FakeTask.adder(100_000);
        });

        while (!async.isDone()) {
            FakeTask.adder(4_000);
        }

        try {
            log.debug("The completed future result is: {}", async.get());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Unexpected in this example: {}", e.getMessage());
        }

        log.trace("Exit");
    }
}
