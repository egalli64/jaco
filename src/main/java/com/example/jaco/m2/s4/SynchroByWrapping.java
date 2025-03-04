/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Avoids race condition when running code originally designed for single-thread
 * execution
 * <p>
 * The original code in Race is NOT modified. Synchronization is applied by wrap
 */
public class SynchroByWrapping {
    private static final Logger log = LoggerFactory.getLogger(SynchroByWrapping.class);

    /**
     * Four threads need to run a non-synchronized method. Provide synchronization
     * as a wrapper.
     * 
     * @param args not used
     * @throws InterruptedException when join is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        System.out.println("Synchronizing on the object before running a non-synchronized method");

        // reference to a legacy object that lacks synchronization
        Race race = new Race();

        // an array of threads wrapping the invoked method to being synchronized
        Thread[] threads = { wrap(race, "Tom"), wrap(race, "Kim"), wrap(race, "Sal"), wrap(race, "Bob") };

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
     * Create a thread wrapping printStatus() for synchronization
     * <p>
     * !!! Assume this is legacy code, see Lock for modern implementation !!!
     * 
     * @param race the legacy object non-synchronized
     * @param name the user name
     * @return a thread that executes printStatus() in a synchronized block
     */
    private static Thread wrap(Race race, String name) {
        return new Thread(() -> {
            synchronized (race) {
                race.printStatus(name);
            }
        });
    }
}
