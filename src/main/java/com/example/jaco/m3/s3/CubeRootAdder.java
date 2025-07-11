/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s3;

import java.util.Arrays;
import java.util.concurrent.atomic.DoubleAdder;

import com.example.jaco.m1.s7.adder.Problem;

/**
 * A DoubleAdder example
 * <p>
 * Meant as a fix to m1.s7.adder.MultiThreadedBuggy
 */
public class CubeRootAdder extends Problem {
    // TODO: number of working threads?
    private static final int WORKER_NR = 8;
    private final int CHUNK_SIZE = data.length / WORKER_NR;

    /**
     * Estimate this algorithm.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        new CubeRootAdder().estimate();
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
     * The result variable is safely shared by workers being atomic
     * <p>
     * Notice that different runs could lead to slightly different results. This is
     * an intrinsic problem caused by rounding and approximation in floating point
     * operations. Given that the additions are potentially performed each time in a
     * different order, we could see tiny changes.
     * <p>
     * If we can't afford it, we should use BigDecimal instead.
     */
    @Override
    protected double calculate() {
        DoubleAdder result = new DoubleAdder();

        Thread[] workers = { //
                new Thread(() -> result.add(partialCubeRootAdder(0))), //
                new Thread(() -> result.add(partialCubeRootAdder(data.length / WORKER_NR))), //
                new Thread(() -> result.add(partialCubeRootAdder(data.length / WORKER_NR * 2))), //
                new Thread(() -> result.add(partialCubeRootAdder(data.length / WORKER_NR * 3))), //
                new Thread(() -> result.add(partialCubeRootAdder(data.length / WORKER_NR * 4))), //
                new Thread(() -> result.add(partialCubeRootAdder(data.length / WORKER_NR * 5))), //
                new Thread(() -> result.add(partialCubeRootAdder(data.length / WORKER_NR * 6))), //
                new Thread(() -> result.add(partialCubeRootAdder(data.length / WORKER_NR * 7))) //
        };

        Arrays.stream(workers).forEach(Thread::start);

        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

        return result.doubleValue();
    }
}
