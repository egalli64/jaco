/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.x1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Execute a Runnable on an Executor as FutureTask
 */
public class SvcExecute1Runnable {
    private static final Logger log = LoggerFactory.getLogger(SvcExecute1Runnable.class);

    /**
     * Create a future task from a Runnable, execute it on an Executor
     * <p>
     * Being the task a Runnable, could be passed to execute(), being a Future,
     * could be used by the caller to interact with its execution on the Executor
     * 
     * @param args not used
     */
    public static void main(String[] args) throws Exception {
        log.trace("Enter");

        FutureTask<String> task = new FutureTask<>(() -> {
            log.trace("Runnable started");
            FakeTask.adder(40_000);
        }, "OK!");

        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            executor.execute(task);

            log.trace("While calculating the future task, do something in the main thread");
            while (!task.isDone()) {
                FakeTask.adder(4);
            }
        }

        try {
            System.out.println("Task result is " + task.get());
        } catch (InterruptedException ex) {
            log.warn("Unexpected", ex);
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            log.error("Error in task execution", ex);
        }

        log.trace("Exit");
    }
}
