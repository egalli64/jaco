/*
 * Introduction to Java Thread
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
     * Create a thread, join with time limit. Maybe the other thread terminate before, maybe not.
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Thread worker = new Thread(() -> {
            log.trace("Enter");

            double result = IntStream.range(2, 1_000).mapToDouble(i -> Math.cbrt(i)).sum();
            System.out.printf("Result is %f%n", result);

            log.trace("Exit");
        }, "worker");

        worker.start();

        try {
            log.trace("Waiting a while {}, then go back to do other stuff", worker.getName());
            worker.join(5);
        } catch (InterruptedException ex) {
            log.warn("This should not happen", ex);
            Thread.currentThread().interrupt();
        }

        // Maybe the worker is still alive, maybe not
        if (worker.isAlive()) {
            log.trace("After timed joining in, {} is still alive", worker.getName());
        } else {
            log.trace("After timed joining in, {} was faster than main thread", worker.getName());
        }

        log.trace("Exit");
    }
}
