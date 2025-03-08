/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s5.ex1;

import java.util.concurrent.RecursiveAction;

/**
 * A RecursiveAction using explicit fork/join
 */
@SuppressWarnings("serial")
class Action extends RecursiveAction {
    private static final int THRESHOLD = 10;
    private final int[] array;
    private final int first;
    private final int end;

    public Action(int[] array, int first, int end) {
        this.array = array;
        this.first = first;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end - first <= THRESHOLD) {
            // base case: process directly if small enough
            for (int i = first; i < end; i++) {
                array[i] += 1;
            }
        } else {
            // split the current task in two subtasks
            int mid = (first + end) / 2;
            Action left = new Action(array, first, mid);
            Action right = new Action(array, mid, end);

            // execute left task asynchronously, process right task recursively
            left.fork();
            right.compute();

            // synchronize the two subtasks
            left.join();
        }
    }
}
