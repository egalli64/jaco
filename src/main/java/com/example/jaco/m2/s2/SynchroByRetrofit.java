/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Avoid race condition when running code designed for single thread execution
 */
public class SynchroByRetrofit {
    private static final Logger log = LoggerFactory.getLogger(SynchroByRetrofit.class);

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

        NoSynchro noSync = new NoSynchro();

        // Each thread runs a synchronized block containing just the call to the
        // non-synchronized method
        // !!! Assume this is legacy code, see Lock for modern implementation !!!
        Thread[] threads = { //
                new Thread(() -> {
                    synchronized (noSync) {
                        noSync.printStatus("Tom");
                    }
                }), new Thread(() -> {
                    synchronized (noSync) {
                        noSync.printStatus("Kim");
                    }
                }), new Thread(() -> {
                    synchronized (noSync) {
                        noSync.printStatus("Sal");
                    }
                }), new Thread(() -> {
                    synchronized (noSync) {
                        noSync.printStatus("Bob");
                    }
                }) //
        };

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        log.trace("Exit");
    }
}
