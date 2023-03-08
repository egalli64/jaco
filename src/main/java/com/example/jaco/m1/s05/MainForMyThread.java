/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s05;

/**
 * Working on a thread by derivation
 */
public class MainForMyThread {
    /**
     * Create and start a thread by instancing a class that extends Thread
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("In thread " + Thread.currentThread().getName());

        // Create another thread defined as extension of class Thread
        Thread other = new MyThread("other");
        System.out.printf("%s state is %s%n", other.getName(), other.getState());

        // Start the other thread
        other.start();
        System.out.printf("%s state is %s%n", other.getName(), other.getState());

        // Doing something else in the main thread
        try {
            // This is just a simulation! The use of sleep() in production code is very limited!
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        // Notice that there is no guarantee that the other thread has already terminated!
        System.out.printf("About to end main, %s state is %s%n", other.getName(), other.getState());
    }
}
