/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m1.s07;

/**
 * Using Thread::isAlive() and Thread::join() with time
 */
public class MainLongRunner {
    /**
     * Create a thread, join with time limit. Maybe the other thread terminate before, maybe not.
     */
    public static void main(String[] args) {
        // Creating a worker with a time consuming job
        Thread t0 = new Thread(() -> {
            System.out.println("Starting a worker");

            double result = 0.0;
            for (int i = 1; i < 30_000_000; i++) {
                result += Math.cbrt(Math.pow(i, Math.PI));
            }

            System.out.printf("Result is %.0f%n", result);
        });

        t0.start();

        try {
            System.out.println("Waiting a while for the other thread, then go back to do other stuff");
            t0.join(20);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        // Maybe the worker is still alive, maybe not
        if (t0.isAlive()) {
            System.out.println("After timed join(), but the worker is still alive");
        } else {
            System.out.println("The worker was so fast that it has already terminated!");
        }

        // In any case, terminate the main thread
        System.out.println("Bye");
    }
}
