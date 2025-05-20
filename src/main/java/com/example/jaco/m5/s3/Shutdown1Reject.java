/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * shutdown() a single thread executor
 */
public class Shutdown1Reject {
    private static final Logger log = LoggerFactory.getLogger(Shutdown1Reject.class);
    private static final int TASK_NR = 5;

    /**
     * Ask to a single thread executor to run a few tasks, but then shutdown
     * <p>
     * Execution of tasks after shutdown is rejected
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        ExecutorService es = Executors.newSingleThreadExecutor();
        for (int i = 0; i < TASK_NR; i++) {
            es.execute(() -> FakeTasks.adder(1_000_000));
        }

        es.shutdown();
        System.out.println("Calling for shutdown");

        try {
            es.execute(() -> System.out.println("This task should be rejected"));
        } catch (RejectedExecutionException ex) {
            log.info("Can't add a task after shutdown");
        }

        // the executor service go on working with the pending tasks
        log.trace("Exit");
    }
}
