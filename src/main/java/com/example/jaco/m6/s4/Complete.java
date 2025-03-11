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
 * Synchronous completion by complete()
 * <p>
 * Only the first call to complete() succeeds, the other ones are ignored
 */
public class Complete {
    private static final Logger log = LoggerFactory.getLogger(Complete.class);

    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<String> cf = new CompletableFuture<>();
        log.debug("Created {}", cf);

        cf.complete("Completed!");
        log.debug("After completing");

        if (cf.complete("ignored")) {
            log.warn("Unexpected! Second complete() succeeded!");
        } else {
            log.info("Can't complete a CompletableFuture more than once");
        }

        try {
            log.info("Get: {}", cf.get());
        } catch (InterruptedException ex) {
            log.warn("Unexpected", ex);
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            log.error("Nobody should throw anything here", ex);
        }

        try {
            log.info("Join result: {}", cf.join());
        } catch (CompletionException ex) {
            log.error("Unexpected", ex);
        }
    }
}
