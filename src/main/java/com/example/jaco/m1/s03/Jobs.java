/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s03;

/**
 * Utility class
 * 
 * Use of Thread::sleep() is just for simple simulation of a busy job
 */
public class Jobs {
    /**
     * It simulates to be busy in doing something for a while.
     * 
     * @param millis time to be busy, in milliseconds
     */
    public static void takeTime(long millis) {
        try {
            System.out.printf("Do something in %s thread for (about) %dms%n", Thread.currentThread().getName(), millis);
            // This is just a simulation! The use of sleep() in production code is very limited!
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
