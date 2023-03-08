/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s05;

/**
 * A thread class, to create threads for its run() method
 * 
 * It is not doing anything special, just some printing and sleeping
 */
public class MyThread extends Thread {
    /**
     * For demo, let the caller set the thread name on creation
     * 
     * @param name the assigned thread name
     */
    public MyThread(String name) {
        super(name);
    }

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
