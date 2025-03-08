/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.x1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Running a FutureTask that returns a Double
 */
public class FutureTask2Callable {
    private static final Logger log = LoggerFactory.getLogger(FutureTask2Callable.class);

    /**
     * Create a future task, run it in another thread, until is not done do
     * something else, then print its result and terminate.
     * 
     * @param args not used
     */
    public static void main(String[] args) throws Exception {
        log.trace("Enter");

        FutureTask<Double> task = new FutureTask<>(() -> {
            log.trace("Future task started");
            return FakeTask.adder(50_000);
        });
        new Thread(task).start();

        log.trace("While calculating the future task, do something in the main thread");
        while (!task.isDone()) {
            FakeTask.adder(5);
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
