/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Executor, ThreadPoolExecutor via Executors.newCachedThreadPool()
 */
public class Executor3CachedThreadPool {
    private static final Logger log = LoggerFactory.getLogger(Executor3CachedThreadPool.class);
    private static final int TASK_NR = 3;

    /**
     * The Cached Thread Pool Executor starts with a core pool sized zero.
     * <p>
     * More threads are created only if required.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Runnable task = () -> {
            FakeTasks.adder(100);
        };

        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            // Pass n Hello runnable to the executor
            Consumer<Integer> batch = n -> IntStream.range(0, n).forEach(i -> executor.execute(task));

            System.out.println("Running the first batch of jobs ...");
            batch.accept(TASK_NR);

            System.out.println("Doing something else in main: " + FakeTasks.adder(100));

            System.out.println("Running the second batch of jobs ...");
            batch.accept(TASK_NR);

            System.out.println("Doing something else in main: " + FakeTasks.adder(100));

            System.out.println("Running the third batch of jobs (double size) ...");
            batch.accept(TASK_NR * 2);
        }

        log.trace("Exit");
    }
}
