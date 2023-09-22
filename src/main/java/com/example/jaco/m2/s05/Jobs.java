/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s05;

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
        log.trace("Enter");
        double result = DoubleStream.generate(() -> Math.random()).limit(size).sum();
        log.trace("Returning {}", result);
        return result;
    }
}
