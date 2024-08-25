/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executor, ThreadPoolExecutor via Executors::newSingleThreadExecutor()
 */
public class SingleThreadExecutor {
    private static final Logger log = LoggerFactory.getLogger(SingleThreadExecutor.class);

    /**
     * In a Single Thread Executor the tasks are serialized to its single thread
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        // implicit executor shutdown, on close() by try with resource (Java 19)
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            Stream.generate(Hello::new).limit(5).forEach(executor::execute);
        }

        log.trace("Exit");
    }
}
