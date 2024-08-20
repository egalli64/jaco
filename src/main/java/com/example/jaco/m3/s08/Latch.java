/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s08;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CountDownLatch
 */
public class Latch extends ProblemFrame {
    private static final Logger log = LoggerFactory.getLogger(Latch.class);

    /**
     * Create a latch set to TASK_NR, run TASK_NR tasks, each task decrease the
     * latch count.
     * <p>
     * Main wait on latch, when the count down is done, gather the result from the
     * tasks and print it.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        DoubleAdder accumulator = new DoubleAdder();
        CountDownLatch cdl = new CountDownLatch(TASK_NR);

        Runnable worker = () -> {
            log.trace("Enter");

            double value = job(100);
            accumulator.add(value);
            cdl.countDown();

            log.trace("Exit value {}, latch {}", value, cdl.getCount());
        };

        Stream.generate(() -> new Thread(worker)).limit(TASK_NR).forEach(t -> t.start());

        try {
            log.trace("Main waits on latch: {}", cdl.getCount());
            cdl.await();
            log.trace("Main sees countdown completed: {}", cdl.getCount());
        } catch (InterruptedException ex) {
            log.warn("Wait unexpectedly interrupted", ex);
            return;
        }
        System.out.printf("Result: %f%n", accumulator.sum());

        log.trace("Exit");
    }
}
