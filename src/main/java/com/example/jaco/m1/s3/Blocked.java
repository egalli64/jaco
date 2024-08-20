/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s3;

import java.lang.Thread.State;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A blocked thread
 * <p>
 * Enable assertion with -ea VM argument
 */
public class Blocked {
    private static final Logger log = LoggerFactory.getLogger(Blocked.class);

    /**
     * Create and starts two threads on a synchronized and time consuming method.
     * <p>
     * The second thread should hang for a while, blocked.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("- Main starts a blocking thread, than another that gets blocked -");
        // Create and start the blocking thread
        new Thread(Blocked::aSynchronizedMethod, "blocking").start();

        // Waste some time before creating and starting another thread
        Jobs.takeTime(50);

        // Create and start another thread on the same method of blocking one
        Thread t1 = new Thread(Blocked::aSynchronizedMethod, "blocked");
        t1.start();

        // Waste some other time, so that the new thread has time to kick in
        Jobs.takeTime(50);

        // The second child thread should be blocked from the blocking thread
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
     * <p>
     * It simulates to be busy in doing something for a while.
     */
    public static synchronized void aSynchronizedMethod() {
        log.trace("Enter");

        try {
            // Just a simulation! The use of sleep() in production code is very limited!
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        log.trace("Exit");
    }
}
