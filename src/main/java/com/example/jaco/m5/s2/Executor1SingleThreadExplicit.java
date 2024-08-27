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
import java.util.stream.Stream;

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
        Stream.generate(() -> new Thread(task)).limit(TASK_NR).forEach(executor::execute);

        // before Java 19, shutdown must be explicit
        executor.shutdown();
        try {
            // give some reasonable time to the pending tasks to terminate
            executor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.warn("Unexpected", e);
            Thread.currentThread().interrupt();
        }
        System.out.println("Result is " + result.sum());

        log.trace("Exit");
    }
}
