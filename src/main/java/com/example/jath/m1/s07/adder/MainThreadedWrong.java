package com.example.jath.m1.s07.adder;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.LongStream;

public class MainThreadedWrong {
    private static final int SIZE = 1_000_000;
    private static final int NR = 10;

    public static void main(String[] args) {
        TreeSet<Double> results = new TreeSet<>();
        List<Long> times = new ArrayList<>();
        long[] data = LongStream.rangeClosed(1, SIZE).toArray();
        System.out.println("Values in data: " + data[0] + " ... " + data[data.length - 1]);

        for (int j = 0; j < NR; j++) {
            long start = System.currentTimeMillis();
            results.add(mtWrongAdder(data));
            times.add(System.currentTimeMillis() - start);
        }

        if (results.size() != 1) {
            System.out.println("THIS IS A BUG! We should get always the same result and not this: " + results);
        } else {
            System.out.println("Result is " + results.first());
        }

        if (results.isEmpty()) {
            System.out.println("No times!");
        } else {
            System.out.println("Time is in " + times);
        }
    }

    private static double partialCubeAdder(long[] data, int begin, int end) {
        double result = 0.0;
        for (int i = begin; i < end; i++) {
            result += Math.cbrt(data[i]);
        }
        return result;
    }

    private static double mtWrongAdder(long[] data) {
        // trick to get around lambda limitation, a local variable could be captured only if final
        // !!! BUGGY! different threads access unsafely the same resource !!!
        // TODO: fix it (remove racing? AtomicLong? synchronization? ...?)
        final double[] result = { 0 };

        Thread[] threads = { //
                new Thread(() -> result[0] += partialCubeAdder(data, 0, data.length / 4)),
                new Thread(() -> result[0] += partialCubeAdder(data, data.length / 4, data.length / 2)),
                new Thread(() -> result[0] += partialCubeAdder(data, data.length / 2, data.length / 4 * 3)),
                new Thread(() -> result[0] += partialCubeAdder(data, data.length / 4 * 3, data.length)) //
        };

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return result[0];
    }
}
