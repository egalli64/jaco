/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s07;

import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Using Thread::isAlive() and Thread::join() with time
 */
public class MainLongRunner {
    private static final Logger log = LoggerFactory.getLogger(MainLongRunner.class);

    /**
     * Create a thread, join with time limit. Maybe the other thread terminate
     * before, maybe not.
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Thread worker = new Thread(() -> {
            log.trace("Enter");

            double result = IntStream.range(2, 1_000).mapToDouble(i -> Math.cbrt(i)).sum();
            System.out.printf("Result is %f%n", result);

            log.trace("Exit");
        });

        worker.start();

        try {
            log.trace("Waiting a while for the worker, then go back to do other stuff");
            // Try changing the wait time to get different behavior
            worker.join(5);
        } catch (InterruptedException ex) {
            log.warn("This should not happen", ex);
            Thread.currentThread().interrupt();
        }

        // Maybe the worker is still alive, maybe not
        if (worker.isAlive()) {
            log.trace("After timed joining in, the worker is still alive");
        } else {
            log.trace("After timed joining in, the worker was faster than main thread");
        }

        log.trace("Exit");
    }
}
