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

import com.example.jaco.m1.s3.FakeTask;

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
            es.execute(() -> FakeTask.adder(1_000_000));
        }

        es.shutdown();
        System.out.println("Shutdown is started");

        try {
            es.execute(() -> System.out.println("Rejected"));
        } catch (RejectedExecutionException ex) {
            log.info("Can't add a task after shutdown");
        }

        log.trace("Exit");
    }
}
