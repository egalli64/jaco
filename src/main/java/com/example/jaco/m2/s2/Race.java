/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s2;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An example of race condition. More threads have access to the same resource.
 */
public class Race {
    private static final Logger log = LoggerFactory.getLogger(Race.class);

    /**
     * Four threads using System.out with no synchronization
     * 
     * @param args not used
     * @throws InterruptedException when join is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        System.out.println("!!! Threads competing on console with no synchronization !!!");

        Race noSync = new Race();
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
     * <p>
     * The output is done on a shared unprotected resource, expect garbling
     * 
     * @param name the user name
     */
    public void printStatus(String name) {
        System.out.printf("Hello, %s. ", name);
        int score = ThreadLocalRandom.current().nextInt(100);
        if (score > 50) {
            System.out.print("Well done! ");
        }
        System.out.printf("Your current score is %d.\n", score);
    }
}
