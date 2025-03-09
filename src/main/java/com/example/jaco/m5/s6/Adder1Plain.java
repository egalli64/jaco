/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s6;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

/**
 * Given an array of doubles, sum of the cubes of its values
 * <p>
 * Single-threaded for-each approach
 */
public class Adder1Plain {
    /**
     * @param args not used
     */
    public static void main(String[] args) {
        double[] data = DoubleStream.generate(ThreadLocalRandom.current()::nextDouble).limit(8_000_000).toArray();

        System.out.println("Plain adder");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();

            double result = 0.0;
            for (double cur : data) {
                result += Math.pow(cur, 3);
            }

            System.out.printf("Sum %f computed in ~ %d ms\n", result, (System.currentTimeMillis() - start));
        }
    }
}
