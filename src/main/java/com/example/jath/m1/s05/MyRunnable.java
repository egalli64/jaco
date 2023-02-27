/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m1.s05;

/**
 * A runnable, to be used for creating and running threads
 * 
 * It is not doing anything special, just some printing and sleeping
 */
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("In thread " + Thread.currentThread().getName());

        try {
            // This is just a simulation! The use of sleep() in production code is very limited!
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        System.out.printf("Thread %s is done%n", Thread.currentThread().getName());
    }
}
