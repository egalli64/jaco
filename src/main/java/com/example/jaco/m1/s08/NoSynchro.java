/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s08;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An example of race condition. More threads have access to the same resource.
 */
public class NoSynchro {
    private static final Logger log = LoggerFactory.getLogger(NoSynchro.class);

    /**
     * Four threads using System.out with no synchronization
     * 
     * @param args not used
     * @throws InterruptedException when join is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        System.out.println("!!! Threads competing on console with no synchronization !!!");

        NoSynchro noSync = new NoSynchro();
        Thread[] threads = { //
                new Thread(() -> noSync.printStatus("Tom")), //
                new Thread(() -> noSync.printStatus("Kim")), //
                new Thread(() -> noSync.printStatus("Sal")), //
                new Thread(() -> noSync.printStatus("Bob")) //
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
     * !!! BUGGED !!! Simulate access to user status and data print.
     * 
     * Since the output is done on a shared unprotected resource, the result could be garbled
     * 
     * @param name the user name
     */
    public void printStatus(String name) {
        System.out.printf("Hello, %s. ", name);
        int score = ThreadLocalRandom.current().nextInt(100);
        if (score > 50) {
            System.out.print("Well done! ");
        }
        System.out.printf("Your current score is %d.%n", score);
    }
}
