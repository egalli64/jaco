/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s6;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Submit a Runnable to an Executor
 */
public class SvcSubmit1Runnable {
    private static final Logger log = LoggerFactory.getLogger(SvcSubmit1Runnable.class);

    /**
     * Create a Runnable, submit it to an Executor
     * <p>
     * The Future returned by submit() allows the caller to interact with the
     * runnable execution on the Executor
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Runnable task = () -> {
            log.trace("Runnable started");
            FakeTask.adder(40_000);
        };

        /*
         * We can force submit() to push into the future a result of a given type
         * 
         * If the result type/value is interesting, a Future<?> could be used instead;
         * in this case, future.get() would return null
         */
        Future<String> future;
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            future = executor.submit(task, "OK!");

            log.trace("While the executor works on the task, do something in the main thread");
            while (!future.isDone()) {
                FakeTask.adder(4);
            }
        }

        try {
            System.out.println("Task result is " + future.get());
        } catch (InterruptedException ex) {
            log.warn("Unexpected", ex);
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            log.error("Error in task execution", ex);
        }

        log.trace("Exit");
    }
}
