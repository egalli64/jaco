/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronous creation of CompletableFuture
 */
public class Completable1Sync {
    private static final Logger log = LoggerFactory.getLogger(Completable1Sync.class);

    public static void main(String[] args) {
        Future<String> incomplete = new CompletableFuture<>();
        log.debug("In an incomplete future the done flag is: {}", incomplete.isDone()); // false
        try {
            incomplete.get(500, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            log.info("Can't get the result of an incomplete future");
        } catch (InterruptedException | ExecutionException e) {
            log.error("Unexpected in this example: {}", e.getMessage());
        }

        Future<String> completed = CompletableFuture.completedFuture("Completed!");
        log.debug("In a completed future the done flag is: {}", completed.isDone()); // true
        try {
            String s = completed.get();
            log.debug("The completed future result is: {}", s);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Unexpected in this example: {}", e.getMessage());
        }

        Future<String> failed = CompletableFuture.failedFuture(new RuntimeException("Failed!"));
        log.debug("Also in a failed future the done flag is: {}", failed.isDone()); // true
        try {
            failed.get();
            log.error("Unexpected in this example");
        } catch (InterruptedException e) {
            log.error("Unexpected in this example: {}", e.getMessage());
        } catch (ExecutionException e) {
            log.info("The failed future completed with exception: {}", e.getMessage());
        }
    }
}
