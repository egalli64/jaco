/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s2;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Executor, ScheduledThreadPoolExecutor via Executors.newScheduledThreadPool()
 */
public class Executor4ScheduledThreadPool {
    private static final Logger log = LoggerFactory.getLogger(Executor4ScheduledThreadPool.class);
    private static final int CORE_POOL_SIZE = 2;

    /**
     * A Scheduled Thread Pool Executor schedules tasks with delays or periodically.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Runnable fixedDelayTask = () -> {
            log.info("Fixed-delay task");
            FakeTask.adder(100);
        };

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

        // run once
        scheduler.schedule(() -> log.info("One-time task"), 500, TimeUnit.MILLISECONDS);
        // run every half sec, first after one third sec
        scheduler.scheduleAtFixedRate(() -> log.info("Fixed-rate task"), 333, 500, TimeUnit.MILLISECONDS);

        // run with a fixed delay half sec, first after one tenth sec
        scheduler.scheduleWithFixedDelay(fixedDelayTask, 100, 500, TimeUnit.MILLISECONDS);

        // the scheduler run for 2 seconds before shutting down
        scheduler.schedule(() -> {
            log.info("Shutting down scheduler");
            scheduler.shutdown();
        }, 2, TimeUnit.SECONDS);

        log.trace("Exit");
    }
}
