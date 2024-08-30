/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s4.task;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * A recursive task for Fork/Join
 */
@SuppressWarnings("serial")
public class CubeAdderRecursiveTask extends RecursiveTask<Double> {
    /** Where the recursion should stop - it is our responsibility to find a good value */
    private static final int THRESHOLD = 200_000;

    private double[] data;
    private int begin;
    private int end;

    /**
     * Constructor
     * 
     * @param data  the full array
     * @param begin left index in the current interval (included)
     * @param end   right index in the current interval (excluded)
     */
    public CubeAdderRecursiveTask(double[] data, int begin, int end) {
        this.data = data;
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Double compute() {
        if (end - begin <= THRESHOLD) {
            double result = 0.0;
            for (int i = begin; i < end; i++) {
                result += Math.pow(data[i], 3);
            }
            return result;
        } else {
            CubeAdderRecursiveTask left = new CubeAdderRecursiveTask(data, begin, (begin + end) / 2);
            CubeAdderRecursiveTask right = new CubeAdderRecursiveTask(data, (begin + end) / 2, end);

            // explicit fork - compute - join
//            right.fork();
//            return left.compute() + right.join();
            
            // implicit fork - compute by ForkJoinTask.invokeAll()
            invokeAll(List.of(left, right));
            return left.join() + right.join();
        }
    }
}
