/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s4;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * CompletableFuture::thenCompose()
 */
public class ThenCompose {
    private static final Logger log = LoggerFactory.getLogger(ThenCompose.class);

    /**
     * Compare use of thenApply and thenCompose when accepting functions that return
     * a CompletableFuture
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        // It works, but it is overly complicated
        CompletableFuture<CompletableFuture<Integer>> applied = completable(10).thenApply(x -> completed(x));
        log.info("The result is {}", applied.join().join());

        // More readable and usable
        CompletableFuture<Integer> composed = completable(10).thenCompose(x -> completed(x));
        log.info("The result is {}", composed.join());

        log.trace("Exit");
    }

    /**
     * A CompletableFuture generator
     * 
     * @param size size of the job
     * @return a completable future that generates a double
     */
    private static CompletableFuture<Double> completable(int size) {
        log.trace("Enter with size {}", size);
        return CompletableFuture.supplyAsync(() -> FakeTask.adder(size));
    }

    /**
     * A completed CompletableFuture generator
     * 
     * @param value a value
     * @return a completed CompletableFuture that generates an integer from the
     *         passed double
     */
    private static CompletableFuture<Integer> completed(double value) {
        log.trace("Enter");
        return CompletableFuture.completedFuture((int) value);
    }
}
