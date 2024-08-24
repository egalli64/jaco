/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m4.s2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executor, ThreadPoolExecutor via Executors::newFixedThreadPool()
 */
public class FixedThreadExecutor {
    private static final Logger log = LoggerFactory.getLogger(FixedThreadExecutor.class);
    private static final int POOL_SIZE = 2;
    private static final int TASK_NR = 5;

    /**
     * The Fixed Thread Pool Executor puts the extra tasks in a queue and run them
     * as soon as a thread gets available.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        // implicit executor shutdown, on close() by try with resource (Java 19)
        try (ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE)) {
            Stream.generate(Hello::new).limit(TASK_NR).forEach(executor::execute);
        }

        log.trace("Exit");
    }
}
