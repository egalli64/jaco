/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Working with two threads, one will interrupt the other one.
 * <p>
 * The console is carelessly shared between threads, expect troubles.
 */
public class Interrupt {
    private static final Logger log = LoggerFactory.getLogger(Interrupt.class);

    /**
     * Create another thread and start it. After a while interrupt it.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        // Behavior of the other thread, run until interrupted
        Runnable runnable = () -> {
            final Thread cur = Thread.currentThread();
            log.trace("Enter");

            int i = 0;
            while (!cur.isInterrupted()) {
                System.out.print("In thread " + cur.getName());
                System.out.print(" ... ");
                System.out.println(i);
                i += 1;
            }

            log.trace("Worker interrupted");
        };

        Thread worker = new Thread(runnable, "worker");

        System.out.println("!!! Race condition on System.out - expect a garbled output !!!");

        worker.start();

        for (int i = 0; i < 30; i++) {
            System.out.print("In thread " + Thread.currentThread().getName());
            System.out.print(" ... ");
            System.out.println(i);
        }

        System.out.println("Thread main asks the worker to terminate");
        worker.interrupt();

        log.trace("Exit");
    }
}
