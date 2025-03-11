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
 * Synchronous completion by obtrudeValue()
 * <p>
 * Forcibly sets or resets the result. Use with extreme caution
 */
public class Obtrude {
    private static final Logger log = LoggerFactory.getLogger(Obtrude.class);

    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<String> cf = new CompletableFuture<>();
        log.debug("Created {}", cf);

        cf.complete("Completed!");

        try {
            log.info("Join after completing: {}", cf.join());
        } catch (CompletionException ex) {
            log.error("No exception expected in this simple example", ex);
        }

        cf.obtrudeValue("Forced reset");

        try {
            log.info("Get after obtruding: {}", cf.get());
        } catch (InterruptedException | ExecutionException ex) {
            log.error("No exception expected in this simple example", ex);
        }
    }
}
