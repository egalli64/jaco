/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s4.action;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Given an array of integers, we want to increment its values
 * <p>
 * Using a ForkJoinPool on a RecursiveAction
 */
public class ArrayIncreaser {
    private static final Logger log = LoggerFactory.getLogger(ArrayIncreaser.class);

    public static void main(String[] args) {
        int[] values = IntStream.range(0, 100).toArray();
        log.info("Original array [{} ... {}]", values[0], values[values.length - 1]);

        try (ForkJoinPool pool = new ForkJoinPool()) {
            ArrayIncreaserRecursiveAction task = new ArrayIncreaserRecursiveAction(values, 0, values.length);
            pool.invoke(task);
        }

        log.info("Increased array [{} ... {}]", values[0], values[values.length - 1]);
    }
}
