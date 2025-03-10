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

import com.example.jaco.m1.s3.FakeTask;

/**
 * Running a FutureTask with a Callable.
 * <p>
 * Basic asynchronous execution and result retrieval
 */
public class FutureTask3Cancel {
    private static final Logger log = LoggerFactory.getLogger(FutureTask3Cancel.class);

    /**
     * Show how to cancel a FutureTask
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
                    // interrupted task can access this value, cancelled one not
                    return result;
                }
                result += FakeTask.adder(1_000);
            }

            log.trace("Task completed");
            return result;
        });

        // start the FutureTask in a new thread
        new Thread(task).start();

        log.trace("While calculating the future task, do something in the main thread");
        double result = FakeTask.adder(10_000);
        System.out.println("Main thread result is " + result);

        // something happens that push to cancel the task
        if (task.cancel(true)) {
            log.trace("Cancel accepted");
        } else {
            log.trace("Cancel rejected");
        }

        try {
            // Attempt to get the result (will throw if cancelled)
            System.out.println("Task result: " + task.get());
        } catch (CancellationException ex) {
            log.info("Task cancelled!");
        } catch (InterruptedException ex) {
            log.warn("Unexpected interrupt while waiting for task result", ex);
            Thread.currentThread().interrupt();
        } catch (ExecutionException ex) {
            log.error("Unexpected task execution error", ex);
        }

        log.trace("Exit");
    }
}
