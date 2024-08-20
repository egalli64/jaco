/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s8;

import java.util.Random;
import java.util.stream.DoubleStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Standard frame for the examples in this package
 */
public class ProblemFrame {
    private static final Logger log = LoggerFactory.getLogger(ProblemFrame.class);
    public static final int TASK_NR = 3;
    private static final Random random = new Random();

    /**
     * A useless job
     * 
     * @param size number of random doubles to sum
     * @return sum of a size random doubles in [0, 1)
     */
    protected static double job(int size) {
        log.trace("Enter {}", size);
        return DoubleStream.generate(random::nextDouble).limit(size).sum();
    }
}
