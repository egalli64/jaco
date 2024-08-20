/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s8;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Avoid race condition even though more threads access the same resource
 */
public class Synchro {
    private static final Logger log = LoggerFactory.getLogger(Synchro.class);

    /**
     * Four threads using System.out in a synchronized method
     * 
     * @param args not used
     * @throws InterruptedException when join is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
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
            thread.join();
        }
        log.trace("Exit");
    }

    /**
     * Simulate access to user status and data print.
     * <p>
     * It is synchronized to protect the changes to System.out. If a thread enter
     * this code, other threads should wait their turn.
     * 
     * @param name the user name
     */
    public synchronized void printStatus(String name) {
        System.out.printf("Hello, %s. ", name);
        int score = ThreadLocalRandom.current().nextInt(100);
        if (score > 50) {
            System.out.print("Well done! ");
        }
        System.out.printf("Your current score is %d.%n", score);
    }
}
