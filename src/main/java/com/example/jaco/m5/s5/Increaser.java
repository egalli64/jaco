/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s5;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Given an array of integers, we want to increment its values
 * <p>
 * Using a ForkJoinPool on a RecursiveAction
 */
public class Increaser {
    private static final Logger log = LoggerFactory.getLogger(Increaser.class);

    public static void main(String[] args) {
        log.trace("Enter");
        int[] values = IntStream.range(0, 100).toArray();
        System.out.printf("Original array [%d ... %d]\n", values[0], values[values.length - 1]);

        try (ForkJoinPool pool = new ForkJoinPool()) {
            Action task = new Action(values, 0, values.length);
            pool.invoke(task);
        }

        System.out.printf("Increased array [%d ... %d] (explicit fork/join)\n", values[0], values[values.length - 1]);

        try (ForkJoinPool pool = new ForkJoinPool()) {
            ActionAll task = new ActionAll(values, 0, values.length);
            pool.invoke(task);
        }

        System.out.printf("Increased array [%d ... %d] (invokeAll)\n", values[0], values[values.length - 1]);
        log.trace("Exit");
    }
}
