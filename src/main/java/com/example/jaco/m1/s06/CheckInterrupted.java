/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s06;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Working with two threads, one will interrupt the other one.
 * 
 * Notice that the console is carelessly shared between threads, expect troubles.
 */
public class CheckInterrupted {
    private static final Logger log = LoggerFactory.getLogger(CheckInterrupted.class);

    /**
     * Create another thread and start it. After a while interrupt it.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        // What is going to be executed by the other thread. Only an interrupt will stop it.
        Runnable runnable = () -> {
            final Thread cur = Thread.currentThread();
            log.trace("Enter");

            int i = 0;
            while (!cur.isInterrupted()) {
                System.out.print("Simulating a job in ");
                System.out.print(cur.getName());
                System.out.print(" ... ");
                System.out.println(i++);
            }

            log.trace("Exit");
        };

        Thread worker = new Thread(runnable, "worker");

        System.out.println("!!! Race condition on System.out - expect a garbled output !!!");

        worker.start();

        for (int i = 0; i < 50; i++) {
            System.out.print("Simulating a job in ");
            System.out.print(Thread.currentThread().getName());
            System.out.print(" ... ");
            System.out.println(i);
        }

        System.out.println("Thread main decides it it time to cut it off");
        worker.interrupt();

        log.trace("Exit");
    }
}
