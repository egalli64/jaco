/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s07;

import java.lang.Thread.State;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Using Thread::isAlive() and Thread::join()
 * 
 * Enable assertions with -ea VM argument
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * Create, start, and join thread, checking if it is alive now and there.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        // Create another thread
        Thread worker = new Thread(() -> System.out.println("A message from the worker"));

        // The worker is NEW, not alive yet
        assert worker.getState() == State.NEW;
        if (!worker.isAlive()) {
            log.trace("Before start, {} is not yet alive", worker.getName());
        }

        worker.start();

        // Uncomment next line to let the worker kick in
//        Jobs.takeTime(10);

        assert worker.getState() == State.RUNNABLE || worker.getState() == State.TERMINATED;
        if (worker.isAlive()) {
            log.trace("After start, the worker could be alive (or terminated)");
        } else {
            log.trace("After start, the worker could be terminated (or alive)");
        }

        try {
            log.trace("Wait on worker termination");
            worker.join();
        } catch (InterruptedException ex) {
            // No one should interrupt the main thread join on the worker
            log.warn("This should not happen", ex);
            // we usually handle the interruption, at least resetting the interrupt flag
            Thread.currentThread().interrupt();
        }

        if (!worker.isAlive()) {
            log.trace("After joining in, the worker is not alive anymore");
        } else {
            log.warn("This should not happen. Maybe join has been interrupted?");
        }
        log.trace("Exit");
    }
}
