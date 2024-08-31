/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s3;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronous completion by completeExceptionally()
 */
public class Complete4Exception {
    private static final Logger log = LoggerFactory.getLogger(Complete4Exception.class);

    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<String> cf = new CompletableFuture<>();
        log.debug("Created {}", cf);

        cf.completeExceptionally(new IllegalStateException("Something unexpected"));
        log.debug("Completed {}", cf);

        try {
            cf.join();
        } catch (Exception ex) {
            log.warn("Exception in the future!", ex);
        }
    }
}
