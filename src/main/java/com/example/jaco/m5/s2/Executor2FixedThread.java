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

import com.example.jaco.m1.s3.FakeTask;

/**
 * Executor, ThreadPoolExecutor via Executors::newFixedThreadPool()
 */
public class Executor2FixedThread {
    private static final Logger log = LoggerFactory.getLogger(Executor2FixedThread.class);
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

        Runnable task = () -> {
            FakeTask.adder(100);
        };

        try (ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE)) {
            Stream.generate(() -> new Thread(task)).limit(TASK_NR).forEach(executor::execute);
        }

        log.trace("Exit");
    }
}
