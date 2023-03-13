/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s05;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A completed future created by CompletableFuture::completedFuture()
 * 
 * No concurrency here. All the job is done in the main thread
 */
public class CFutureCompleted {
    private static final Logger log = LoggerFactory.getLogger(CFutureCompleted.class);

    /**
     * Create the completable future, do some other job, then wait for the completable future
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Main");
        CompletableFuture<Double> cf = CompletableFuture.completedFuture(Jobs.job(10));

        if (cf.isDone()) {
            log.trace("The future is done, still I have some job to do");
            System.out.printf("Main thread result: %f%n", Jobs.job(10));
        }

        System.out.printf("Completable future result: %f%n", cf.join());
        log.trace("Exit");
    }
}
