/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m1.s06;

/**
 * A thread that ignore the standard interrupt approach but could be interrupted following a custom
 * approach.
 */
public class MyInterruptibleThread extends Thread {
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
     * Being a package private method, only from this package a thread of this could be terminated.
     */
    void shutdown() {
        done = true;
    }

    @Override
    public void run() {
        System.out.println("In thread " + getName());

        int i = 0;
        // loop until shutdown
        while (!done) {
            System.out.print("Simulating " + getName() + " wait on a resource ... ");
            try {
                // This is just a simulation! The use of sleep() in production code is very limited!
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught and ignored!");
            }
            System.out.println(i++);
        }

        System.out.printf("Thread %s is done%n", getName());
    }
}
