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
 * Synchronous completion by obtrudeValue()
 * <p>
 * Forcibly sets or resets the result
 */
public class Complete3Obtrude {
    private static final Logger log = LoggerFactory.getLogger(Complete3Obtrude.class);

    public static void main(String[] args) {
        log.trace("Enter");

        CompletableFuture<String> cf = new CompletableFuture<>();
        log.debug("Created {}", cf);

        cf.complete("Completed!");

        if (cf.isDone()) {
            log.debug("A first join, before obtruding: {}", cf.join());

            cf.obtrudeValue("Forced reset");
        } else {
            log.warn("Unexpected!");
        }

        log.info("A second join: {}", cf.join());
    }
}
