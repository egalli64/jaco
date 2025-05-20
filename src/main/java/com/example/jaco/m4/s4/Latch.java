/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m4.s4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * CountDownLatch
 */
public class Latch {
    private static final int TASK_NR = 3;
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
        CountDownLatch latch = new CountDownLatch(TASK_NR);

        Runnable worker = () -> {
            log.trace("Enter");

            // Simulate the work done by the thread
            double value = FakeTasks.adder(100);
            accumulator.add(value);
            latch.countDown();

            log.trace("Exit value {}, latch is currently {}", value, latch.getCount());
        };

        Stream.generate(() -> new Thread(worker)).limit(TASK_NR).forEach(t -> t.start());

        try {
            log.trace("Main waits on latch: {}", latch.getCount());
            latch.await();
            log.trace("Main sees countdown completed: {}", latch.getCount());
        } catch (InterruptedException ex) {
            log.warn("Wait on latch unexpectedly interrupted", ex);
        }
        System.out.printf("Result: %.2f\n", accumulator.sum());

        log.trace("Exit");
    }
}
