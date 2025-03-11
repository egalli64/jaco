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
 * Synchronous completion by obtrudeException()
 * <p>
 * Forcibly sets or resets the exception. Use with extreme caution
 */
public class ObtrudeException {
    private static final Logger log = LoggerFactory.getLogger(ObtrudeException.class);

    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<String> cf = new CompletableFuture<>();
        log.debug("Created {}", cf);

        cf.obtrudeException(new ArrayIndexOutOfBoundsException("Something unexpected"));

        try {
            cf.join();
        } catch (CompletionException ex) {
            log.info("CompletionException caused by {}: {}", //
                    ex.getCause().getClass().getSimpleName(), ex.getCause().getMessage());
        }

        cf.obtrudeException(new IllegalStateException("Something else unexpected"));

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
