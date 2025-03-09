/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s5;

import java.util.concurrent.RecursiveAction;

/**
 * A RecursiveAction using invokeAll() for implicit fork/join
 */
@SuppressWarnings("serial")
class ActionAll extends RecursiveAction {
    private static final int THRESHOLD = 10;
    private final int[] array;
    private final int first;
    private final int end;

    public ActionAll(int[] array, int first, int end) {
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
            ActionAll left = new ActionAll(array, first, mid);
            ActionAll right = new ActionAll(array, mid, end);

            // invoke both tasks concurrently and synchronize automatically
            invokeAll(left, right);
        }
    }
}
