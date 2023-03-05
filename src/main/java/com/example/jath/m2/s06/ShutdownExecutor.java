/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s06;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * ExecutorService, shutdown() vs shutdownNow()
 */
public class ShutdownExecutor {
    private static final int TASK_NR = 5;

    /**
     * Ask to a single thread executor to run TASK_NR Runnables, then shutdown.
     * 
     * A task can't be added to a shutdown executor
     * 
     * @param args if at least an argument is passed, call shutdownNow(), otherwise shutdown()
     */
    public static void main(String[] args) {
        System.out.println("Single Thread Executor on Runnables");
        ExecutorService es = Executors.newSingleThreadExecutor();
        for (int i = 0; i < TASK_NR; i++) {
            es.execute(() -> System.out.println("Hello " + Math.cbrt(Math.random())));
        }

        if (args.length == 0) {
            List<Runnable> canceled = es.shutdownNow();
            System.out.printf("%d tasks have been canceled%n", canceled.size());
        } else {
            es.shutdown();
        }

        try {
            es.execute(() -> System.out.println("Rejected"));
        } catch (RejectedExecutionException ex) {
            System.out.println("Can't add a task after shutdown");
        }
    }
}