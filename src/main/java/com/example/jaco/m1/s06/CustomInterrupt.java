/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s06;

/**
 * User of MyInterruptibleThread, show how to terminate a thread using a custom approach
 */
public class CustomInterrupt {
    /**
     * Create and start a MyInterruptibleThread. Standard interrupt is ignored, use shutdown() instead.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("In thread " + Thread.currentThread().getName());

        MyInterruptibleThread other = new MyInterruptibleThread("other");

        System.out.println("!!! Two threads are going to compete on System.out !!!");
        System.out.println("!!! Output could be garbled  !!!");

        other.start();

        try {
            System.out.println("Simulating a job in main, let other thread having some fun ...");
            // This is just a simulation! The use of sleep() in production code is very limited!
            Thread.sleep(50);

            // Ask other to interrupt, but it does not cooperate
            System.out.println("Interrupting other thread");
            other.interrupt();
            other.join(50);
            if (other.isAlive()) {
                System.out.println("Interrupt rejected");
            }

            // Ask other to shutdown, and this approach is accepted
            System.out.println("Shutting down other thread");
            other.shutdown();
            other.join(50);
            if (!other.isAlive()) {
                System.out.println("Shutdown accepted");
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        System.out.println("Bye");
    }
}
