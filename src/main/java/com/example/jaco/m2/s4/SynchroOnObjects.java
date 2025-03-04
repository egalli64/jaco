/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fine-grained synchronization using multiple locks
 * <p>
 * !!! Assume this is legacy code, see Lock for modern implementation !!!
 */
public class SynchroOnObjects {
    private static final Logger log = LoggerFactory.getLogger(SynchroOnObjects.class);

    private final Object lockA = new Object();
    private int resourceA = 0;
    private final Object lockB = new Object();
    private int resourceB = 0;

    /**
     * Eight working threads divided in four groups
     * <li>Synchronized on the current object (this - standard) for System.out
     * <li>Synchronized on lock "A" for resourceA
     * <li>Synchronized on lock "B" for resourceB
     * <li>Synchronized on lock "A" and then "B" for both resources
     * 
     * @param args not used
     * @throws InterruptedException when join is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        SynchroOnObjects soo = new SynchroOnObjects();

        Thread[] threads = { //
                new Thread(soo::syncThis, "This1"), new Thread(soo::syncA, "A1"), //
                new Thread(soo::syncB, "B1"), new Thread(soo::syncAB, "AB1"), //
                new Thread(soo::syncThis, "This2"), new Thread(soo::syncA, "A2"), //
                new Thread(soo::syncB, "B2"), new Thread(soo::syncAB, "AB2") //
        };

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        log.trace("Exit");
    }

    /**
     * Since A and B are read-only in this method, no race condition occurs
     */
    public synchronized void syncThis() {
        log.trace("Enter, lock on this");
        System.out.printf("Accordingly to %s A is %d, B is %d\n", //
                Thread.currentThread().getName(), resourceA, resourceB);
        log.trace("Exit, unlock on this");
    }

    /**
     * Synchronization for resourceA
     */
    public void syncA() {
        log.trace("Enter");
        synchronized (lockA) {
            log.trace("Lock on A");
            resourceA += 1;
            log.trace("Unlock on A");
        }
        log.trace("Exit");
    }

    /**
     * Synchronization for resourceB
     */
    public void syncB() {
        log.trace("Enter");
        synchronized (lockB) {
            log.trace("Lock on B");
            resourceB += 1;
            log.trace("Unlock on B");
        }
        log.trace("Exit");
    }

    /**
     * Synchronization for A and then B
     * <p>
     * !!! If another method need synchronization on both resources, the same order
     * should be used, otherwise deadlocks may occur !!!
     * <p>
     * Consider using tryLock() with timeout in modern implementations
     * 
     */
    public void syncAB() {
        log.trace("Enter");
        synchronized (lockA) {
            log.trace("Lock on A");
            resourceA *= 2;
            log.trace("Unlock on A");
        }

        synchronized (lockB) {
            log.trace("Lock on B");
            resourceB *= 2;
            log.trace("Unlock on B");
        }
        log.trace("Exit");
    }
}
