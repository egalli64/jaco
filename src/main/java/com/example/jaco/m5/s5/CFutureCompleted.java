/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s5;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * A completed future created by CompletableFuture::completedFuture()
 * <p>
 * No parallelism here. All the job is done in the main thread
 */
public class CFutureCompleted {
    private static final Logger log = LoggerFactory.getLogger(CFutureCompleted.class);

    /**
     * Create the completable future, do some other job, then wait for the
     * completable future
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Main");
        CompletableFuture<Double> cf = CompletableFuture.completedFuture(FakeTask.adder(100));

        if (cf.isDone()) {
            log.trace("The future is done, still I have some job to do");
            System.out.println("Main thread result: " + FakeTask.adder(10));
        } else {
            throw new IllegalStateException("The future is expected to be completed");
        }

        System.out.println("Completable future result: " + cf.join());
        log.trace("Exit");
    }
}
