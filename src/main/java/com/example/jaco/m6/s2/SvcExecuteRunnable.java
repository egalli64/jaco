/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Execute a Runnable on an Executor: fire and forget
 * <p>
 * No Future involved
 */
public class SvcExecuteRunnable {
    private static final Logger log = LoggerFactory.getLogger(SvcExecuteRunnable.class);

    /**
     * Asynchronous execution with no Future involved
     * 
     * @param args not used
     */
    public static void main(String[] args) throws Exception {
        log.trace("Enter");

        Runnable task = () -> {
            log.trace("Runnable started");
            FakeTasks.adder(40_000);
        };

        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            executor.execute(task);

            log.trace("Main thread continues execution...");
            for (int i = 0; i < 6; i++) {
                FakeTasks.adder(1_000);
            }
        }

        log.trace("Exit");
    }
}
