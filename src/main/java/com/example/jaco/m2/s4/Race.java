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
 * An example of race condition. Multiple threads access the same resource
 * (System.out) without synchronization, leading to garbled output
 */
public class Race {
    private static final Logger log = LoggerFactory.getLogger(Race.class);

    /**
     * Four threads writing to the console without synchronization
     * 
     * @param args not used
     * @throws InterruptedException if join is interrupted
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

        System.out.println("Done");
        log.trace("Exit");
    }

    /**
     * !!! UNSAFE !!!
     * 
     * Simulate access to user status and print data without synchronization
     * <p>
     * System.out is a shared resource and there is no synchronization. Output from
     * different threads may interleave unpredictably
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
