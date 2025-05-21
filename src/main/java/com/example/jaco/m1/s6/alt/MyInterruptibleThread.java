/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s6.alt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A thread that ignore the standard interrupt approach but could be interrupted
 * following a custom approach
 */
public class MyInterruptibleThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(MyInterruptibleThread.class);

    /**
     * Custom termination flag (it is volatile to ensure visibility across threads)
     * 
     * @implNote do not call it "interrupted", as the original flag in Thread. It
     *           would be legal but confusing
     */
    private volatile boolean done;

    /**
     * Constructor
     * 
     * @param name the thread name
     */
    public MyInterruptibleThread(String name) {
        super(name);
        this.done = false;
    }

    /**
     * Accept a termination request
     * <p>
     * It is intentionally package private, only from this package a
     * MyInterruptibleThread could be terminated
     */
    void shutdown() {
        log.trace("Enter");
        done = true;
    }

    @Override
    public void run() {
        log.trace("Enter");

        int i = 0;
        // loop until shutdown
        while (!done) {
            System.out.print("(Fake) wait on a resource ... ");
            try {
                // Just a simulation! Thread.sleep() in seldom seen in production code
                Thread.sleep(10);
            } catch (InterruptedException e) {
                log.warn("Interrupted ignored!");
            }
            System.out.print(i);
            i += 1;
            System.out.println(" calculated");
        }

        log.trace("Exit");
    }
}
