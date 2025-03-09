/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s6;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

/**
 * Given an array of doubles, sum of the cubes of its values
 * <p>
 * Single-threaded stream approach
 */
public class Adder2Stream {
    /**
     * @param args not used
     */
    public static void main(String[] args) {
        double[] data = DoubleStream.generate(ThreadLocalRandom.current()::nextDouble).limit(8_000_000).toArray();

        System.out.println("Single-threaded streaming");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            double result = Arrays.stream(data).map(x -> Math.pow(x, 3)).sum();
            System.out.printf("Sum %f computed in ~ %d ms\n", result, (System.currentTimeMillis() - start));
        }
    }
}
