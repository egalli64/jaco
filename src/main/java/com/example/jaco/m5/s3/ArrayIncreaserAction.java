/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s3;

import java.util.concurrent.RecursiveAction;

/**
 * A RecursiveAction
 */
@SuppressWarnings("serial")
class ArrayIncreaserAction extends RecursiveAction {
    private static final int THRESHOLD = 10;
    private final int[] array;
    private final int first;
    private final int end;

    public ArrayIncreaserAction(int[] array, int first, int end) {
        this.array = array;
        this.first = first;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end - first <= THRESHOLD) {
            // if the size is "small enough" do the actual job
            for (int i = first; i < end; i++) {
                array[i] += 1;
            }
        } else {
            // otherwise split it in two subtasks
            int mid = (first + end) / 2;
            ArrayIncreaserAction left = new ArrayIncreaserAction(array, first, mid);
            ArrayIncreaserAction right = new ArrayIncreaserAction(array, mid, end);

            // asynchronous execute a subtask
            left.fork();
            right.compute();
            // synchronize the two subtasks
            left.join();
        }
    }
}
