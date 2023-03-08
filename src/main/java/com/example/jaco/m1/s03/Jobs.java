/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class
 * 
 * Use of Thread::sleep() is just for simple simulation of a busy job
 */
public class Jobs {
    private static final Logger log = LoggerFactory.getLogger(Jobs.class);

    /**
     * It simulates to be busy in doing something for a while.
     * 
     * @param millis time to be busy, in milliseconds
     */
    public static void takeTime(long millis) {
        try {
            log.trace("Do something for (about) {} ms", millis);
            // This is just a simulation! The use of sleep() in production code is very limited!
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
