/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.DoubleAdder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Executor, ThreadPoolExecutor via Executors::newSingleThreadExecutor()
 */
public class Executor1SingleThread {
    private static final Logger log = LoggerFactory.getLogger(Executor1SingleThread.class);
    private static final int TASK_NR = 100;
    private static final int TASK_SIZE = 100_000;

    /**
     * In a Single Thread Executor the tasks are serialized to its single thread
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");
        DoubleAdder result = new DoubleAdder();

        Runnable task = () -> {
            double cur = FakeTasks.calc(TASK_SIZE);
            log.trace("Current value is {}", cur);
            result.add(cur);
        };

        final long start = System.currentTimeMillis();
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            for (int i = 0; i < TASK_NR; i++) {
                executor.execute(task);
            }
        }
        System.out.printf("Result is %.2f (%d ms)\n", result.sum(), System.currentTimeMillis() - start);

        log.trace("Exit");
    }
}
