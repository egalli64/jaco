/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A few fake tasks
 */
public class FakeTasks {
    private static final Logger log = LoggerFactory.getLogger(FakeTasks.class);

    /**
     * Simulate to be busy doing something for a while
     * 
     * @param millis required duration
     * @throws IllegalStateException if interrupted during sleep
     */
    public static void takeTime(long millis) {
        try {
            log.trace("Do something for (about) {} ms", millis);
            // In production code you won't see often calls to Thread.sleep()
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Thread was interrupted during sleep", e);

            throw new IllegalStateException(e);
        }
    }

    /**
     * Sum up a few random doubles
     * 
     * @param size number of items used to generate the result
     * @return a double
     */
    public static double adder(int size) {
        double result = DoubleStream.generate(ThreadLocalRandom.current()::nextDouble).limit(size).sum();
        log.trace("Returning {}", result);
        return result;
    }

    /**
     * Taking some time performing a useless calculation
     * 
     * @param size number of items used to generate the result
     * @return a double
     */
    public static double calc(int size) {
        return DoubleStream.generate(ThreadLocalRandom.current()::nextDouble).limit(size).map(Math::cbrt).sum();
    }
}
