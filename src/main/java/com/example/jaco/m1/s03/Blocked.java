/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s03;

import java.lang.Thread.State;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A blocked thread
 * 
 * Enable assertion with -ea VM argument
 */
public class Blocked {
    private static final Logger log = LoggerFactory.getLogger(Blocked.class);

    /**
     * Create and starts two threads on a synchronized time consuming method.
     * 
     * The second one should hang for a while, blocked.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        String tName = "worker";

        System.out.printf("- A thread, %s, that could be blocked -%n", tName);
        // Create and start another thread that runs before on the same method
        new Thread(Blocked::aSynchronizedMethod, "blocking").start();

        // The main thread does something else before creating and starting another thread
        Jobs.takeTime(50);

        // Create and start a thread, that should run the same method of "blocking" one
        Thread t1 = new Thread(Blocked::aSynchronizedMethod, tName);
        t1.start();

        // Waste some other time, so we can assume that the new thread has time to kick in
        Jobs.takeTime(50);

        // Since both child thread run on the same synchronized method, the second should be blocked
        assert t1.getState() == State.BLOCKED;
        System.out.printf("Thread %s is %s%n", t1.getName(), t1.getState());

        // Keep the main thread busy in a long job
        Jobs.takeTime(900);

        // Now both children should have ended their job
        assert t1.getState() == State.TERMINATED;
        System.out.printf("Thread %s is %s%n", t1.getName(), t1.getState());

        System.out.println("- Main is about to terminate -");
    }

    /**
     * The execution of this method is protected by an implicit lock on the class
     * 
     * It simulates to be busy in doing something for a while.
     */
    public static synchronized void aSynchronizedMethod() {
        log.trace("Enter");

        try {
            // This is just a simulation! The production use of sleep() is very limited!
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        log.trace("Exit");
    }
}
