/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m1.s07.adder;

import java.util.Arrays;

/**
 * Is multithreading worth?
 * 
 * Using a full fledged class for the worker as in the MultiThreaded example looks like a bit of an
 * overkill. Lambdas here could help in making the code more compact and readable.
 * 
 * However, here the code has a BUG caused by RACE CONDITION (more on it later)
 */
public class MultiThreadedBuggy extends Problem {
    private static final int WORKER_NR = 8;
    private final int CHUNK_SIZE = data.length / WORKER_NR;

    /**
     * Run an estimation for this (BUGGED) algorithm
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        new MultiThreadedBuggy().estimate();
    }

    /**
     * Each thread would work on its own chunk of data
     * 
     * @param begin the index of the first element for the current chunk
     * @return result for the current chunk
     */
    private double partialCubeRootAdder(int begin) {
        return Arrays.stream(data).skip(begin).limit(CHUNK_SIZE).mapToDouble(x -> Math.cbrt(x)).sum();
    }

    /**
     * !!! BUGGY! different threads access unsafely the same resource !!!
     * 
     * TODO: fix it (synchronization? atomic variables?)
     */
    @Override
    protected double calculate() {
        // a local variable could be captured by a lambda only if it is effectively final
        double[] result = { 0.0 };

        // !!! BUG: result is written by more threads and not protected from race condition !!!
        Thread[] workers = { //
                new Thread(() -> result[0] += partialCubeRootAdder(0)), //
                new Thread(() -> result[0] += partialCubeRootAdder(CHUNK_SIZE)), //
                new Thread(() -> result[0] += partialCubeRootAdder(CHUNK_SIZE * 2)), //
                new Thread(() -> result[0] += partialCubeRootAdder(CHUNK_SIZE * 3)), //
                new Thread(() -> result[0] += partialCubeRootAdder(CHUNK_SIZE * 4)), //
                new Thread(() -> result[0] += partialCubeRootAdder(CHUNK_SIZE * 5)), //
                new Thread(() -> result[0] += partialCubeRootAdder(CHUNK_SIZE * 6)), //
                new Thread(() -> result[0] += partialCubeRootAdder(CHUNK_SIZE * 7)) //
        };

        for (Thread worker : workers) {
            worker.start();
        }

        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

        return result[0];
    }
}
