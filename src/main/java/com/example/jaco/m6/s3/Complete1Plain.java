/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronous completion by complete()
 * <p>
 * Only the first call to complete() succeeds, the next ones are ignored
 */
public class Complete1Plain {
    private static final Logger log = LoggerFactory.getLogger(Complete1Plain.class);

    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<String> cf = new CompletableFuture<>();
        log.debug("Created {}", cf);

        cf.complete("Completed!");

        if (cf.isDone()) {
            log.debug("After completing");

            cf.complete("Being already completed, this request is ignored");
        } else {
            log.warn("Unexpected!");
        }

        try {
            log.info("Get: {}", cf.get());
        } catch (InterruptedException ex) {
            log.warn("Unexpected", ex);
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            log.error("Nobody should throw anything here", ex);
        }

        log.info("Join: {}", cf.join());
    }
}
