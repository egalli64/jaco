/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s7.adder;

import java.util.Arrays;

/**
 * Is multithreading worth?
 * <p>
 * Let's try a CPU intensive calculation using more threads using a Java 8
 * Stream feature
 */
public class MultiThreadedStream extends Problem {
    /**
     * Calculate a simple cost estimate for calculate()
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        new MultiThreadedStream().estimate();
    }

    /**
     * Let Java do the dirty job by parallel()
     */
    @Override
    protected double calculate() {
        return Arrays.stream(data).parallel().mapToDouble(x -> Math.cbrt(x)).sum();
    }
}
