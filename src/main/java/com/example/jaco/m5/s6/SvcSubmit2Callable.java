/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s6;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Submit a Callable to an Executor
 */
public class SvcSubmit2Callable {
    private static final Logger log = LoggerFactory.getLogger(SvcSubmit2Callable.class);

    /**
     * Create a Callable, submit it to an Executor
     * <p>
     * The Future returned by submit() allows the caller to interact with the
     * callable execution on the Executor
     * 
     * @param args not used
     */
    public static void main(String[] args) throws Exception {
        log.trace("Enter");

        Callable<Double> task = () -> {
            log.trace("Callable started");
            return FakeTask.adder(50_000);
        };

        // the future type should match with the callable type
        Future<Double> future;
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            future = executor.submit(task);

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
