/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s7.adder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Basic setup for a problem to see if multithreading is worth of all its
 * trouble
 */
public abstract class Problem {
    /** Number of elements to work with */
    public static final int SIZE = 1_600_000;
    /** Number of test repetitions */
    public static final int NR = 10;

    /** Each run would generate a result, hopefully, always the same one */
    protected Set<Double> results = new HashSet<>(NR);
    /** Each run would take some time, reported here */
    protected List<Long> times = new ArrayList<>(NR);
    /** Data sample */
    protected int[] data;

    /**
     * Constructor
     * <p>
     * All the concrete classes should work on the same data
     */
    public Problem() {
        results = new HashSet<>();
        times = new ArrayList<>();
        data = IntStream.rangeClosed(1, SIZE).toArray();
    }

    /**
     * Repeat NR times the calculation to have a rough time estimation.
     * <p>
     * Then print the result.
     */
    public void estimate() {
        for (int i = 0; i < NR; i++) {
            long start = System.currentTimeMillis();
            results.add(calculate());
            times.add(System.currentTimeMillis() - start);
        }

        System.out.println("Results: " + results);
        System.out.println("Times in millis: " + times);
    }

    /**
     * Each different approach would calculate the result in a different way
     * 
     * @return the sum of the cubic root of each element in the data sample
     */
    abstract protected double calculate();
}
