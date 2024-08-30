/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s4.action;

import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * A recursive action for Fork/Join
 * <p>
 * For a task like this, a RecursiveTask is a better approach
 */
@SuppressWarnings("serial")
public class CubeAdderRecursiveAction extends RecursiveAction {
    /**
     * it is our responsibility to set where the recursion should stop
     */
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
    public CubeAdderRecursiveAction(double[] data, int begin, int end) {
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
            CubeAdderRecursiveAction left = new CubeAdderRecursiveAction(data, begin, (begin + end) / 2);
            CubeAdderRecursiveAction right = new CubeAdderRecursiveAction(data, (begin + end) / 2, end);

            invokeAll(List.of(left, right));

            // same as calling ForkJoinTask.invokeAll(), but explicitly applying the fork /
            // join pattern
//            left.fork();
//            right.compute();
//            left.join();

            // for both approaches, in the end set the joined result
            result = left.result + right.result;
        }
    }

    /**
     * Getter - it's a bit unnatural using a RecursiveAction when a result is
     * expected from the task. Use RecursiveTask instead.
     * 
     * @return the expected result
     */
    public double result() {
        return result;
    }
}
