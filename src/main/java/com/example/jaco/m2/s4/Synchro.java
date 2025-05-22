/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s4;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fix race condition by synchronizing access to System.out
 * <p>
 * Ensure that only one thread at a time can execute printStatus()
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

        System.out.println("Done");
        log.trace("Exit");
    }

    /**
     * Simulate access to user status and print data
     * <p>
     * It is synchronized to protect System.out, only one thread can execute it at a
     * time. Other threads attempting to executed it will block until the current
     * thread completes.
     * 
     * @param name the user name
     */
    private synchronized void printStatus(String name) {
        System.out.printf("Hello, %s. ", name);
        int score = ThreadLocalRandom.current().nextInt(100);
        if (score > 50) {
            System.out.print("Well done! ");
        }
        System.out.printf("Your current score is %d.%n", score);
    }
}
