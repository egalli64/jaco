/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s3;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Thread communication - synchronization by volatile and atomic variable
 */
public class AtomicIntegerExample {
    private static final Logger log = LoggerFactory.getLogger(AtomicIntegerExample.class);

    /** Written only by main */
    private static volatile boolean run = true;
    /** Written only by worker, but requires CAS operations */
    private static AtomicInteger counter = new AtomicInteger(0);

    /**
     * Main thread and runner communicates over a volatile and an atomic variable.
     * No need of synchronization
     * 
     * @param args not used
     * @throws InterruptedException - actually, no interrupt is expected here
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");

        // super-intensive CPU bound task, this is not meant to be real code!
        Thread worker = new Thread(() -> {
            log.trace("Enter");
            // volatile is not enough, increasing is not an atomic operation
            while (run) {
                counter.incrementAndGet();
            }
            log.trace("Exit");
        }, "worker");
        worker.start();

        // do something in main thread, give time to worker thread to kick in
        FakeTasks.takeTime(1);

        // run is written - worker needs to see its actual value
        run = false;
        worker.join();

        // counter is read - main needs to see its actual value
        System.out.println("Calculated by worker: " + counter);
        log.trace("Exit");
    }
}
