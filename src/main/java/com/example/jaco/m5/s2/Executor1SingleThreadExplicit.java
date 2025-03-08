/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.DoubleAdder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Executor, ThreadPoolExecutor via Executors::newSingleThreadExecutor()
 */
public class Executor1SingleThreadExplicit {
    private static final Logger log = LoggerFactory.getLogger(Executor1SingleThreadExplicit.class);
    private static final int TASK_NR = 10;
    private static final int TASK_SIZE = 100;

    /**
     * In a Single Thread Executor the tasks are serialized to its single thread
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");
        DoubleAdder result = new DoubleAdder();

        Runnable task = () -> {
            double cur = FakeTask.calc(TASK_SIZE);
            log.trace("Current value is {}", cur);
            result.add(cur);
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < TASK_NR; i++) {
            executor.execute(task);
        }

        // before Java 19, shutdown must be explicit
        executor.shutdown();
        try {
            // give some reasonable time to the pending tasks to terminate
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                log.warn("Executor did not terminate in the expected time");
            }
        } catch (InterruptedException e) {
            log.warn("Interrupted while waiting for tasks to finish", e);
            Thread.currentThread().interrupt();
        }
        System.out.printf("Result is %.2f\n", result.sum());

        log.trace("Exit");
    }
}
