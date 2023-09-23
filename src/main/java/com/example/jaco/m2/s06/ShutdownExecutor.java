/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s06;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExecutorService, shutdown() vs shutdownNow()
 * <p>
 * Single Thread Executor on Runnables
 */
public class ShutdownExecutor {
    private static final Logger log = LoggerFactory.getLogger(ShutdownExecutor.class);
    private static final int TASK_NR = 5;

    /**
     * Ask to a single thread executor to run TASK_NR Runnables, then shutdown.
     * <p>
     * A task can't be added to an executor after shutdown
     * 
     * @param args determine if shutdownNow() or shutdown() should be called
     */
    public static void main(String[] args) {
        log.trace("Enter");

        ExecutorService es = Executors.newSingleThreadExecutor();
        for (int i = 0; i < TASK_NR; i++) {
            es.execute(() -> System.out.printf("Result is %f%n", Math.cbrt(Math.random())));
        }

        if (args.length == 0) {
            List<Runnable> canceled = es.shutdownNow();
            log.info("{} tasks have been canceled", canceled.size());
        } else {
            es.shutdown();
        }

        try {
            es.execute(() -> System.out.println("Rejected"));
        } catch (RejectedExecutionException ex) {
            log.info("Can't add a task after shutdown");
        }

        log.trace("Exit");
    }
}