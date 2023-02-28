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
 * Let's try a CPU intensive calculation using more threads "by hand"
 */
public class MultiThreaded extends Problem {
    /**
     * Calculate a simple cost estimate for calculate()
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        new MultiThreaded().estimate();
    }

    /**
     * Split the job in 8 and give each part to a different thread, then put the result together.
     * 
     * The code is not complicated, but it is too low level.
     * 
     * TODO: why 8 jobs and not 4, or 16? This magic number is machine dependent!
     */
    @Override
    protected double calculate() {
        final int subLen = data.length / 8;
        Worker[] workers = { //
                new Worker(0, subLen), //
                new Worker(data.length / 8, subLen), //
                new Worker(data.length / 4, subLen), //
                new Worker(data.length / 8 * 3, subLen), //
                new Worker(data.length / 2, subLen), //
                new Worker(data.length / 8 * 5, subLen), //
                new Worker(data.length / 4 * 3, subLen), //
                new Worker(data.length / 8 * 7, subLen) //
        };

        for (Worker worker : workers) {
            worker.start();
        }

        for (Worker worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

        return Arrays.stream(workers).mapToDouble(Worker::getResult).sum();
    }

    /**
     * The worker for the current problem.
     * 
     * Being an inner class, it could access "data". Notice that this is a read-only access.
     */
    private class Worker extends Thread {
        private int begin;
        private int size;
        private double result = 0.0;

        /**
         * Constructor
         * 
         * @param begin index of the first element of data for this worker
         * @param size  size of the slice of data for this worker
         */
        Worker(int begin, int size) {
            this.begin = begin;
            this.size = size;
        }

        /**
         * Operate just on the current slice of competence
         */
        @Override
        public void run() {
            result = Arrays.stream(data).skip(begin).limit(size).mapToDouble(x -> Math.cbrt(x)).sum();
        }

        /**
         * Getter
         * 
         * @return the worker result
         */
        public double getResult() {
            return result;
        }
    }
}
