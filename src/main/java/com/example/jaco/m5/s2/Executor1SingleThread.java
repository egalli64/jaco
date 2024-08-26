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
 * Executor, ThreadPoolExecutor via Executors::newSingleThreadExecutor()
 */
public class Executor1SingleThread {
    private static final Logger log = LoggerFactory.getLogger(Executor1SingleThread.class);

    /**
     * In a Single Thread Executor the tasks are serialized to its single thread
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Runnable task = () -> {
            log.trace("Enter");
            System.out.printf("On %s generated value is %f\n", Thread.currentThread().getName(), FakeTask.calc(100));
            log.trace("Exit");
        };

        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            Stream.generate(() -> new Thread(task)).limit(5).forEach(executor::execute);
        }

        log.trace("Exit");
    }
}
