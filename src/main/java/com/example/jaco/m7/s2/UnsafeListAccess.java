/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * "Normal" collection are not thread-safe
 */
public class UnsafeListAccess {
    private static final int LIST_SIZE = 10;
    private static final int THREAD_NR = 5;
    private static final int RUN_NR = 1_000;

    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>(Collections.nCopies(LIST_SIZE, 0));

        Runnable task = () -> {
            for (int i = 0; i < RUN_NR; i++) {
                for (int j = 0; j < list.size(); j++) {
                    // !!! DANGER - not thread-safe !!!
                    list.set(j, list.get(j) + 1);
                }
            }
        };

        try (ExecutorService executor = Executors.newFixedThreadPool(THREAD_NR)) {
            for (int i = 0; i < THREAD_NR; i++) {
                executor.execute(task);
            }
        }

        int result = list.stream().mapToInt(Integer::intValue).sum();
        System.out.printf("Expecting %d, result is %d\n", LIST_SIZE * THREAD_NR * RUN_NR, result);
    }
}
