/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s2;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Running a FutureTask with a Callable
 * <p>
 * Basic asynchronous execution and partial result retrieval (interrupt)
 */
public class FutureTask4Interrupt {
    private static final Logger log = LoggerFactory.getLogger(FutureTask4Interrupt.class);

    /**
     * Show how let a FutureTask generate a partial result when interrupted
     * 
     * @param args not used
     */
    public static void main(String[] args) throws Exception {
        FutureTask<Double> task = new FutureTask<>(() -> {
            // Simulate a long-running task that can be interrupted
            log.trace("Task started");
            double result = 0.0;

            for (int i = 0; i < 40; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    log.trace("Task interrupted!");
                    // interrupted task will get this partial result
                    return result;
                }
                result += FakeTasks.adder(1_000);
            }

            log.trace("Task completed");
            return result;
        });

        Thread thread = new Thread(task);
        thread.start();

        log.trace("While calculating the future task, do something in the main thread");
        double result = FakeTasks.adder(10_000);
        System.out.println("Main thread result is " + result);

        // something happens that push to interrupt the task
        thread.interrupt();

        try {
            System.out.println("Task (possibly partial) result: " + task.get());
        } catch (CancellationException | InterruptedException | ExecutionException ex) {
            log.error("Unexpected exception", ex);
        }

        log.trace("Exit");
    }
}
