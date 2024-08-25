/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s8;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Standard frame for the examples in this package
 */
public class ProblemFrame {
    public static final int TASK_NR = 3;

    /**
     * Wrap the adder fake task
     * 
     * @param size number of random doubles to sum
     * @return sum of a size random doubles in [0, 1)
     */
    protected static double adder(int size) {
        return FakeTask.adder(size);
    }
}
