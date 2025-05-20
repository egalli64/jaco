/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Submit a Callable to an Executor
 */
public class SvcSubmit2Callable {
    private static final Logger log = LoggerFactory.getLogger(SvcSubmit2Callable.class);

    /**
     * Create a Callable, submit it to an Executor
     * <p>
     * The Future returned by submit() allows the caller to interact with the
     * Callable execution
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Callable<Double> task = () -> {
            log.trace("Callable started");
            return FakeTasks.adder(40_000);
        };

        // the Future type should match with the Callable parametric type
        Future<Double> future;
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            future = executor.submit(task);

            log.trace("While the executor works on the task, do something in the main thread");
            while (!future.isDone()) {
                FakeTasks.adder(4);
            }
        }

        try {
            System.out.println("Task result is " + future.get());
        } catch (InterruptedException ex) {
            log.warn("Unexpectedly interrupted while waiting for the task to complete", ex);
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            log.error("Error in task execution", ex);
        }

        log.trace("Exit");
    }
}
