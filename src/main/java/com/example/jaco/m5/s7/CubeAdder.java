/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s7;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.DoubleStream;

/**
 * Fork Join on a RecursiveTask
 */
public class CubeAdder {
    /**
     * <ul>
     * <li>Define a RecursiveTask, pass the data to it
     * <li>On a Fork Join Pool, call invoke on the recursive action
     * <li>Extract the result from the recursive action
     * 
     * @param data the values to evaluate
     * @return sum of elements cubes
     */
    public static Future<Double> recursiveAction(double[] data) {
        CubeAdderTask task = new CubeAdderTask(data, 0, data.length);
        try (var pool = new ForkJoinPool()) {
            pool.invoke(task);
            return task;
        }
    }

    /**
     * Use of Fork Join on a RecursiveTask
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        double[] data = DoubleStream.generate(Math::random).limit(8_000_000).toArray();

        System.out.println("-Fork Join Recursive Task-");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            try {
                Future<Double> result = recursiveAction(data);
                System.out.printf("Sum %f computed in ~ %d ms%n", result.get(), (System.currentTimeMillis() - start));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
