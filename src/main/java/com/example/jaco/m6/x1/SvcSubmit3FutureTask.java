/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.x1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Submit a FutureTask to an Executor
 */
public class SvcSubmit3FutureTask {
    private static final Logger log = LoggerFactory.getLogger(SvcSubmit3FutureTask.class);

    /**
     * Create a FutureTask, submit it to an Executor
     * <p>
     * The Future returned gives access to the executing task as the one passed in
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        FutureTask<Double> task = new FutureTask<>(() -> {
            log.trace("Future task started");
            return FakeTask.adder(40_000);
        });

        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            // the task is considered as a Runnable, so the result generic type is lost
            Future<?> future = executor.submit(task);

            log.trace("While the executor works on the task, do something in the main thread");
            // the check could be done on task, there's no actual need of future here
            while (!future.isDone()) {
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
