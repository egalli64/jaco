/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s04;

/**
 * To run the main method the main thread is created and started by the JVM
 */
public class Main {
    public static void main(String[] args) {
        System.out.printf("Running thread %s%n", Thread.currentThread().getName());

        System.out.printf("About to sleep @ %d%n", System.currentTimeMillis());
        try {
            // This is just a simulation! The use of sleep() in production code is very limited!
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted: " + e.getMessage());
        }

        System.out.printf("...sleep ended @ %d%n", System.currentTimeMillis());
    }
}
