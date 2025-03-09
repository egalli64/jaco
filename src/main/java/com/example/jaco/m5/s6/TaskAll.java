/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s6;

import java.util.concurrent.RecursiveTask;

/**
 * A recursive task for Fork/Join - invokeAll()
 */
@SuppressWarnings("serial")
public class TaskAll extends RecursiveTask<Double> {
    /** It is our responsibility to determine where the recursion should stop */
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
    public TaskAll(double[] data, int begin, int end) {
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
            int middle = (begin + end) / 2;
            TaskAll left = new TaskAll(data, begin, middle);
            TaskAll right = new TaskAll(data, middle, end);

            invokeAll(left, right);
            return left.join() + right.join();
        }
    }
}
