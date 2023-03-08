/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s08;

import java.util.Random;

/**
 * Avoid race condition even though more threads have access to the same resource.
 */
public class Synchro {
    private final Random random = new Random();

    /**
     * Four threads using System.out in a synchronized method
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("Threads competing on console _with_ synchronization");

        Synchro synchro = new Synchro();
        Thread[] threads = { //
                new Thread(() -> synchro.printStatus("Tom")), //
                new Thread(() -> synchro.printStatus("Kim")), //
                new Thread(() -> synchro.printStatus("Sal")), //
                new Thread(() -> synchro.printStatus("Bob")) //
        };

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * Simulate access to user status and data print.
     * 
     * It is synchronized to protect the changes to System.out. If a thread enter this code, other
     * threads should wait their turn to enter.
     * 
     * @param name the user name
     */
    public synchronized void printStatus(String name) {
        System.out.printf("Hello, %s. ", name);
        int score = random.nextInt(100);
        if (score > 50) {
            System.out.print("Well done! ");
        }
        System.out.printf("Your current score is %d.%n", score);
    }
}
