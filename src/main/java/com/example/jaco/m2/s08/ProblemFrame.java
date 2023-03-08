/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s08;

import java.util.Random;
import java.util.stream.DoubleStream;

/**
 * Standard frame for the examples in this package
 */
public class ProblemFrame {
    public static final int TASK_NR = 3;
    private static final Random random = new Random();

    /**
     * A useless job
     * 
     * @param size number of random doubles to sum
     * @return sum of a size random doubles in [0, 1)
     */
    protected static double job(int size) {
        return DoubleStream.generate(random::nextDouble).limit(size).sum();
    }
}
