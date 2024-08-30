/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s4.task;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.DoubleStream;

/**
 * Fork Join on a RecursiveTask
 */
public class CubeAdder {
    /**
     * <ul>
     * <li>Define a RecursiveTask, pass the data to it
     * <li>On a Fork Join Pool, call invoke on the recursive task
     * <li>Join on the recursive task to get the result
     * 
     * @param data the values to evaluate
     * @return sum of elements cubes
     */
    public static double recursiveTask(double[] data) {
        CubeAdderRecursiveTask task = new CubeAdderRecursiveTask(data, 0, data.length);
        try (var pool = new ForkJoinPool()) {
            pool.invoke(task);
            return task.join();
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
            double result = recursiveTask(data);
            System.out.printf("Sum %f computed in ~ %d ms%n", result, (System.currentTimeMillis() - start));
        }

        System.out.println("-Parallel stream adder-");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            double result = Arrays.stream(data).parallel().map(x -> Math.pow(x, 3)).sum();
            System.out.printf("Sum %f computed in ~ %d ms\n", result, (System.currentTimeMillis() - start));
        }
    }
}
