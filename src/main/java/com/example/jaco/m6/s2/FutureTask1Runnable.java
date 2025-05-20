/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Running a FutureTask with a Runnable
 * <p>
 * Basic asynchronous execution and result retrieval
 */
public class FutureTask1Runnable {
    private static final Logger log = LoggerFactory.getLogger(FutureTask1Runnable.class);

    /**
     * Create a FutureTask with a Runnable, start it in a new thread, perform other
     * tasks in the main thread, and then retrieve the result
     * 
     * @param args not used
     */
    public static void main(String[] args) throws Exception {
        log.trace("Enter");

        // a FutureTask with a Runnable and the result to be retrieved by get()
        FutureTask<String> task = new FutureTask<>(() -> {
            log.trace("Runnable started");
            FakeTasks.adder(40_000);
        }, "OK!");

        // start the FutureTask in a new thread
        new Thread(task).start();

        log.trace("While calculating the future task, do something in the main thread");
        double result = 0.0;

        // do other tasks while the FutureTask is running
        while (!task.isDone()) {
            result += FakeTasks.adder(4);
        }
        System.out.println("Result in the main thread is " + result);

        try {
            // block until the task is completed, then get its result
            System.out.println("Task result is " + task.get());
        } catch (InterruptedException ex) {
            log.warn("Task interrupted unexpectedly", ex);
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            log.error("Unexpected error in task execution", ex);
        }

        log.trace("Exit");
    }
}
