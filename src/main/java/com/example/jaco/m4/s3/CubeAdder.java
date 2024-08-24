/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m4.s3;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

/**
 * Given an array of doubles, we want the sum of the cubes of its values.
 * 
 * Comparing Fork-Join approach with plain sum and use of parallel stream.
 */
public class CubeAdder {
    /**
     * Single thread classic approach by for-each
     * 
     * @param data the values to evaluate
     * @return sum of elements cubes
     */
    public static double plain(double[] data) {
        double result = 0;
        for (double cur : data) {
            result += Math.pow(cur, 3);
        }
        return result;
    }

    /**
     * Multithreaded approach by Fork Join
     * 
     * <li>Define a RecursiveAction, pass the data to it
     * <li>On a Fork Join Pool, call invoke on the recursive action - notice that
     * since Java 19 ExecutorService is AutoCloseable
     * <li>Extract the result from the recursive action
     * 
     * @param data the values to evaluate
     * @return sum of elements cubes
     */
    public static double recursiveAction(double[] data) {
        CubeAdderAction action = new CubeAdderAction(data, 0, data.length);

        try (var pool = new ForkJoinPool()) {
            pool.invoke(action);
            return action.result();
        }
    }

    /**
     * Parallel stream approach
     * 
     * @param data the values to evaluate
     * @return sum of elements cubes
     */
    public static double parallelStream(double[] data) {
        return Arrays.stream(data).parallel().map(x -> Math.pow(x, 3)).sum();
    }

    /**
     * Comparison of approaches.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        double[] data = DoubleStream.generate(ThreadLocalRandom.current()::nextDouble).limit(8_000_000).toArray();

        System.out.println("Plain adder");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            double result = plain(data);
            System.out.printf("Sum %f computed in ~ %d ms\n", result, (System.currentTimeMillis() - start));
        }

        System.out.println("Fork Join Recursive Action");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            double result = recursiveAction(data);
            System.out.printf("Sum %f computed in ~ %d ms\n", result, (System.currentTimeMillis() - start));
        }

        System.out.println("Parallel stream adder");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            double result = parallelStream(data);
            System.out.printf("Sum %f computed in ~ %d ms\n", result, (System.currentTimeMillis() - start));
        }
    }
}
