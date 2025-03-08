/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * delegate the shutdown() of a single thread executor to try with resources
 * 
 * @apiNote auto-closeable ExecutorService is a Java 19
 */
public class Shutdown4Modern {
    private static final Logger log = LoggerFactory.getLogger(Shutdown4Modern.class);
    private static final int TASK_NR = 5;

    /**
     * Ask to a single thread executor to run a few tasks in a try with resources
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        // implicit shutdown
        try (ExecutorService es = Executors.newSingleThreadExecutor()) {
            for (int i = 0; i < TASK_NR; i++) {
                es.execute(() -> FakeTask.adder(1_000_000));
            }
        }

        log.trace("Exit");
    }
}
