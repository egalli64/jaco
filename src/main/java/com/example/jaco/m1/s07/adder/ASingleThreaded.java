/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s07.adder;

import java.util.Arrays;

/**
 * Is multithreading worth?
 * <p>
 * Let's try a CPU intensive calculation following a single thread approach
 */
public class ASingleThreaded extends Problem {
    /**
     * Calculate a simple cost estimate for calculate()
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        new ASingleThreaded().estimate();
    }

    /**
     * Very simple code on a single thread
     */
    @Override
    protected double calculate() {
        return Arrays.stream(data).mapToDouble(x -> Math.cbrt(x)).sum();
    }
}
