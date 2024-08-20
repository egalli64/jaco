/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A thread that ignore the standard interrupt approach but could be interrupted
 * following a custom approach.
 */
public class MyInterruptibleThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(MyInterruptibleThread.class);

    /** Used as the interrupted flag */
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
     * Accept a termination request.
     * 
     * Package private method, only from this package a MyInterruptibleThread could
     * be terminated.
     */
    void shutdown() {
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
                // Just a simulation! Thread::sleep() in seldom seen in production code
                Thread.sleep(10);
            } catch (InterruptedException e) {
                log.warn("Interrupted ignored!");
            }
            System.out.println(i++ + " calculated");
        }

        log.trace("Exit");
    }
}
