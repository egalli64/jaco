/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s5;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for job simulations
 */
public class Jobs {
    private static final Logger log = LoggerFactory.getLogger(Jobs.class);

    /**
     * A simple job
     * 
     * @param size number of items used to generate the result
     * @return a double
     */
    public static double job(int size) {
        double result = DoubleStream.generate(ThreadLocalRandom.current()::nextDouble).limit(size).sum();
        log.trace("Returning {}", result);
        return result;
    }
}
