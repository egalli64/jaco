package com.example.jath.m1.s11;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.LongStream;

public class CubeRootAdder {
    private static final int SIZE = 1_000_000;
    private static final int NR = 10;

    public static void main(String[] args) {
        Set<Double> results = new TreeSet<>();
        List<Long> times = new ArrayList<>();
        long[] data = LongStream.rangeClosed(1, SIZE).toArray();
        System.out.println("Values in data: " + data[0] + " ... " + data[data.length - 1]);

        for (int j = 0; j < NR; j++) {
            long start = System.currentTimeMillis();
            results.add(cubeRootAdder(data));
            times.add(System.currentTimeMillis() - start);
        }

        if (results.size() != 1) {
            System.out.println("Unexpected number of results!");
        } else {
            System.out.printf("Result is %f%n", results.iterator().next());
        }

        if (results.isEmpty()) {
            System.out.println("No times!");
        } else {
            System.out.println("Time is in " + times);
        }
    }

    private static double partialCubeRootAdder(long[] data, int begin, int end) {
        double result = 0.0;
        for (int i = begin; i < end; i++) {
            result += Math.cbrt(data[i]);
        }
        return result;
    }

    private static double cubeRootAdder(long[] data) {
        DoubleAdder result = new DoubleAdder();

        Thread[] threads = { //
                new Thread(() -> result.add(partialCubeRootAdder(data, 0, data.length / 4))),
                new Thread(() -> result.add(partialCubeRootAdder(data, data.length / 4, data.length / 2))),
                new Thread(() -> result.add(partialCubeRootAdder(data, data.length / 2, data.length / 4 * 3))),
                new Thread(() -> result.add(partialCubeRootAdder(data, data.length / 4 * 3, data.length))) //
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
        return result.doubleValue();
    }
}
