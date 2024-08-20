/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Thread communication - weak synchronization by volatile.
 * <p>
 * More threads could work on a volatile variable, but only one of them should
 * be allowed to change it.
 */
public class Volatile {
    private static final Logger log = LoggerFactory.getLogger(Volatile.class);

    /** Written only by main */
    private static volatile boolean run = true;
    /** Written only by worker */
    private static volatile int counter = 0;

    /**
     * Main thread and runner communicates over two volatile variables. No need of
     * synchronization.
     * 
     * @param args not used
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        Thread worker = new Thread(() -> {
            log.trace("Enter");
            // run is read
            while (run) {
                // counter is written
                counter += 1;
            }
            log.trace("Exit");
        }, "worker");
        worker.start();

        // do something in main thread, give time to worker thread to kick in
        FakeTask.takeTime(0);

        // run is written
        run = false;
        worker.join();

        // counter is read
        System.out.println("Calculated by worker: " + counter);
        log.trace("Exit");
    }
}
