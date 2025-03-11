/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s4;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronous completion by completeExceptionally()
 */
public class CompleteExceptionally {
    private static final Logger log = LoggerFactory.getLogger(CompleteExceptionally.class);

    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<String> cf = new CompletableFuture<>();
        log.debug("Created: {}", cf);

        cf.completeExceptionally(new RuntimeException("Something unexpected"));
        log.debug("Completed: {}", cf);

        try {
            cf.join();
        } catch (CompletionException ex) {
            log.info("CompletionException caused by {}: {}", //
                    ex.getCause().getClass().getSimpleName(), ex.getCause().getMessage());
        }

        try {
            cf.get();
        } catch (InterruptedException ex) {
            log.error("Unexpected in this example", ex);
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            log.info("ExecutionException caused by {}: {}", //
                    ex.getCause().getClass().getSimpleName(), ex.getCause().getMessage());
        }
    }
}
