/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s03;

import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * A recursive action for Fork/Join
 */
@SuppressWarnings("serial")
public class CubeAdderAction extends RecursiveAction {
    /** Where the recursion should stop - it is our responsibility to find a good value */
    private static final int THRESHOLD = 200_000;

    private double[] data;
    private int begin;
    private int end;
    private double result;

    /**
     * Constructor
     * 
     * @param data  the full array
     * @param begin left index in the current interval (included)
     * @param end   right index in the current interval (excluded)
     */
    public CubeAdderAction(double[] data, int begin, int end) {
        this.data = data;
        this.begin = begin;
        this.end = end;
        this.result = 0;
    }

    @Override
    protected void compute() {
        if (end - begin <= THRESHOLD) {
            for (int i = begin; i < end; i++) {
                result += Math.pow(data[i], 3);
            }
        } else {
            CubeAdderAction left = new CubeAdderAction(data, begin, (begin + end) / 2);
            CubeAdderAction right = new CubeAdderAction(data, (begin + end) / 2, end);

            ForkJoinTask.invokeAll(List.of(left, right));

            // same of calling invokeAll, but explicitly applying the fork / join pattern
//            left.fork();
//            right.compute();
//            left.join();
//
//            result = left.result + right.result;
        }
    }

    /**
     * Getter
     * 
     * @return the expected result
     */
    public double result() {
        return result;
    }
}
