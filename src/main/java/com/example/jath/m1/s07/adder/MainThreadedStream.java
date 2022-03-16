package com.example.jath.m1.s07.adder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.LongStream;

public class MainThreadedStream {
    private static final int SIZE = 1_000_000;
    private static final int NR = 10;

    public static void main(String[] args) {
        TreeSet<Double> results = new TreeSet<>();
        List<Long> times = new ArrayList<>();
        long[] data = LongStream.rangeClosed(1, SIZE).toArray();
        System.out.println("Values in data: " + data[0] + " ... " + data[data.length - 1]);

        for (int j = 0; j < NR; j++) {
            long start = System.currentTimeMillis();
            results.add(Arrays.stream(data).parallel().mapToDouble(x -> Math.cbrt(x)).sum());
            times.add(System.currentTimeMillis() - start);
        }

        if (results.size() != 1) {
            System.out.println("Unexpected number of results!");
        } else {
            System.out.printf("Result is %f%n", results.first());
        }

        if (results.isEmpty()) {
            System.out.println("No times!");
        } else {
            System.out.println("Time is in " + times);
        }
    }
}
