/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s3;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * shutdownNow() a single thread executor
 */
public class Shutdown2Now {
    private static final Logger log = LoggerFactory.getLogger(Shutdown2Now.class);
    private static final int TASK_NR = 5;

    /**
     * Ask to a single thread executor to run a few tasks, but then shutdown (now!)
     * <p>
     * Pending tasks not assigned to a thread are canceled
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        ExecutorService es = Executors.newSingleThreadExecutor();
        for (int i = 0; i < TASK_NR; i++) {
            es.execute(() -> FakeTask.adder(1_000_000));
        }

        List<Runnable> canceled = es.shutdownNow();
        System.out.println("Calling for shutdown");

        log.info("{} tasks have been canceled", canceled.size());

        try {
            if (!es.awaitTermination(1, TimeUnit.SECONDS)) {
                log.warn("Executor did not terminate in time");
            } else {
                log.info("Executor terminated successfully");
            }
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for termination", e);
            Thread.currentThread().interrupt();
        }

        log.trace("Exit");
    }
}
