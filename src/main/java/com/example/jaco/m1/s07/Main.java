/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s07;

/**
 * Using Thread::isAlive() and Thread::join()
 */
public class Main {
    /**
     * Create, start, and join thread, checking if it is alive now and there.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        // Create a thread
        Thread t0 = new Thread(() -> System.out.println("A message from another thread"));

        // It is not alive yet
        if (!t0.isAlive()) {
            System.out.printf("Before starting it, %s is not yet alive%n", t0.getName());
        } else {
            throw new IllegalStateException("You should not get here");
        }

        t0.start();

        // After starting it is alive
        if (t0.isAlive()) {
            System.out.printf("After starting it, %s is alive%n", t0.getName());
        } else {
            throw new IllegalStateException("You should not get here");
        }

        try {
            // the current thread wait the other thread to terminate
            t0.join();
            System.out.println(t0.getName() + " joined");
        } catch (InterruptedException e) {
            System.out.printf("Main thread interrupted while waiting %s to join%n" + t0.getName());
            // we usually handle the interruption, at least resetting the interrupt flag
            Thread.currentThread().interrupt();
        }

        // After joining the other thread should not be alive anymore
        if (!t0.isAlive()) {
            System.out.println("The other thread is not alive anymore");
        } else {
            System.out.println("You should not get this message, join interrupted?");
        }
    }
}
