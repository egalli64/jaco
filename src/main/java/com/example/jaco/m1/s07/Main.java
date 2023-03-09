/*
 * Introduction to Java Thread
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
        String name = "worker";
        // Create another thread
        Thread worker = new Thread(() -> System.out.printf("A message from %s%n", name), name);

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
            log.trace("After start, now {} could be alive", worker.getName());
        } else {
            log.trace("After start, now {} could be terminated", worker.getName());
        }

        try {
            log.trace("Wait {} to terminate", worker.getName());
            worker.join();
        } catch (InterruptedException ex) {
            // No one should interrupt the main thread join on the worker
            log.warn("This should not happen", ex);
            // we usually handle the interruption, at least resetting the interrupt flag
            Thread.currentThread().interrupt();
        }

        if (!worker.isAlive()) {
            log.trace("After joining in, {} is not alive anymore", worker.getName());
        } else {
            log.warn("This should not happen. Join interrupted?");
        }
        log.trace("Exit");
    }
}
