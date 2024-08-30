/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m5.s7.CFutureBasicIntro;

/**
 * Synchronous creation of CompletableFuture
 */
public class Creation1Sync {
    private static final Logger log = LoggerFactory.getLogger(CFutureBasicIntro.class);

    public static void main(String[] args) {
        Future<String> incompleted = new CompletableFuture<>();
        log.debug("The done flag for an incomplete completable future is {}", incompleted.isDone());

        Future<String> completed = CompletableFuture.completedFuture("Completed!");
        log.debug("The done flag for a completed completable future is {}", completed.isDone());
    }
}
