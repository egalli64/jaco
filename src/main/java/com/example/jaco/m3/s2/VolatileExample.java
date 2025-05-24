/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Thread communication - weak synchronization by volatile
 * <p>
 * More threads could work on a volatile variable, but only one of them should
 * be allowed to change it
 */
public class VolatileExample {
    private static final Logger log = LoggerFactory.getLogger(VolatileExample.class);

    /** Written only by main */
    private static volatile boolean run = true;
    /** Written only by worker */
    private static volatile int counter = 0;

    /**
     * Main thread and runner communicates over two volatile variables. No need of
     * synchronization
     * 
     * @param args not used
     * @throws InterruptedException - no interrupt is expected here
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");

        // super-intensive CPU bound task, this is not meant to be real code!
        Thread worker = new Thread(() -> {
            log.trace("Enter");
            // run is read - worker needs to see its actual value
            while (run) {
                // counter is written only by the worker
                // notice that volatile ensures visibility but not atomicity!
                // see AtomicInteger for a more robust implementation
                counter += 1;
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
