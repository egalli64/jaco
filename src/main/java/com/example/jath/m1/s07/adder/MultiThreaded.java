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
 * Let's try a CPU intensive calculation using more threads "by hand". The idea is dividing the job
 * among a few threads to exploit the multithreading hardware available on the current machine.
 * 
 * Here I'm splitting the job in 8 parts. Is that a good choice?
 */
public class MultiThreaded extends Problem {
    private static final int WORKER_NR = 8;
    private final int CHUNK_SIZE = data.length / WORKER_NR;

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
     */
    @Override
    protected double calculate() {
        Worker[] workers = { new Worker(0), new Worker(CHUNK_SIZE), new Worker(CHUNK_SIZE * 2),
                new Worker(CHUNK_SIZE * 3), new Worker(CHUNK_SIZE * 4), new Worker(CHUNK_SIZE * 5),
                new Worker(CHUNK_SIZE * 6), new Worker(CHUNK_SIZE * 7) //
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
     * Being an inner class, it could access "data". Notice that this is a read-only access. Each thread
     * puts the result of its work in variable of its own ownership.
     */
    private class Worker extends Thread {
        private int begin;
        private double result = 0.0;

        /**
         * Constructor
         * 
         * @param begin index of the first element of data for this worker
         */
        Worker(int begin) {
            this.begin = begin;
        }

        /**
         * Operate just on the current slice of competence
         */
        @Override
        public void run() {
            result = Arrays.stream(data).skip(begin).limit(CHUNK_SIZE).mapToDouble(x -> Math.cbrt(x)).sum();
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
