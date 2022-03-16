package com.example.jath.m1.s07.adder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.LongStream;

public class MainSingle {
    private static final int SIZE = 1_000_000;
    private static final int NR = 10;

    public static void main(String[] args) {
        Set<Double> results = new TreeSet<>();
        List<Long> times = new ArrayList<>();
        long[] data = LongStream.rangeClosed(1, SIZE).toArray();
        System.out.println("Values in data: " + data[0] + " ... " + data[data.length - 1]);

        for (int j = 0; j < NR; j++) {
            long start = System.currentTimeMillis();
            results.add(plainCubeRootAdder(data));
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

    private static double plainCubeRootAdder(long[] data) {
        double result = 0.0;
        for (long value : data) {
            result += Math.cbrt(value);
        }
        return result;
    }
}
