/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s6;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.DoubleStream;

/**
 * Fork Join with a RecursiveTask
 */
public class CubeAdder {
    /**
     * Compute the sum of cubes using a RecursiveTask with explicit fork/join
     * 
     * @param data the values to evaluate
     * @return sum of elements cubes
     */
    public static double forkJoin(double[] data) {
        Task task = new Task(data, 0, data.length);
        try (var pool = new ForkJoinPool()) {
            return pool.invoke(task);
        }
    }

    /**
     * Compute the sum of cubes using a RecursiveTask with invokeAll()
     * 
     * @param data the values to evaluate
     * @return sum of elements cubes
     */
    public static double forkJoinAll(double[] data) {
        TaskAll task = new TaskAll(data, 0, data.length);
        try (var pool = new ForkJoinPool()) {
            return pool.invoke(task);
        }
    }

    /**
     * Use of Fork Join with RecursiveTask
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        double[] data = DoubleStream.generate(Math::random).limit(8_000_000).toArray();

        System.out.println("-Fork Join Recursive Task (explicit fork/join)-");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            double result = forkJoin(data);
            System.out.printf("Sum %f computed in ~ %d ms%n", result, (System.currentTimeMillis() - start));
        }

        System.out.println("-Fork Join Recursive Task (invokeAll)-");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            double result = forkJoinAll(data);
            System.out.printf("Sum %f computed in ~ %d ms%n", result, (System.currentTimeMillis() - start));
        }
    }
}
