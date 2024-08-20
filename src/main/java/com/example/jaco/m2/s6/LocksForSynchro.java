/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s6;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Finer synchronization, using ReentrantLock.
 * <p>
 * Compare it to {@link com.example.jaco.m1.s8.SynchroOnObjects}
 */
public class LocksForSynchro {
    private static final Logger log = LoggerFactory.getLogger(LocksForSynchro.class);

    private final Lock lockA;
    private int resourceA;
    private Lock lockB;
    private int resourceB;

    /**
     * Constructor
     */
    public LocksForSynchro() {
        this.lockA = new ReentrantLock();
        this.resourceA = 0;

        this.lockB = new ReentrantLock();
        this.resourceB = 0;
    }

    /**
     * Eight working threads divided in four groups
     * <li>Synchronized on this (standard) for System.out
     * <li>Synchronized on lock "A" for resourceA
     * <li>Synchronized on lock "B" for resourceB
     * <li>Synchronized on lock "A" and then "B" for both resources
     * 
     * @param args not used
     * @throws InterruptedException when join is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        LocksForSynchro lfs = new LocksForSynchro();

        Thread[] threads = { //
                new Thread(lfs::syncThis, "This1"), //
                new Thread(lfs::syncA, "A1"), //
                new Thread(lfs::syncB, "B1"), //
                new Thread(lfs::syncAB, "AB1"), //
                new Thread(lfs::syncThis, "This2"), //
                new Thread(lfs::syncA, "A2"), //
                new Thread(lfs::syncB, "B2"), //
                new Thread(lfs::syncAB, "AB2") //
        };

        Arrays.stream(threads).forEach(Thread::start);
        for (Thread t : threads) {
            t.join();
        }

        log.trace("Exit");
    }

    /**
     * Synchronization for System.out
     * <p>
     * A and B are in read-only access, then there is no race condition on them
     */
    public synchronized void syncThis() {
        log.trace("Enter");
        System.out.printf("%s A is %d, B is %d%n", Thread.currentThread().getName(), resourceA, resourceB);
        log.trace("Exit");
    }

    /**
     * Synchronization for resourceA
     */
    public void syncA() {
        log.trace("Enter");
        synchronized (lockA) {
            log.trace("Lock A acquired");
            resourceA += 1;
        }
        log.trace("Lock A released, exit");
    }

    /**
     * Synchronization for resourceB
     */
    public void syncB() {
        log.trace("Enter");
        synchronized (lockB) {
            log.trace("Lock B acquired");
            resourceB += 1;
        }
        log.trace("Lock B released, exit");
    }

    /**
     * Synchronization for A and then B
     */
    public void syncAB() {
        log.trace("Enter");
        synchronized (lockA) {
            log.trace("Lock A acquired");
            resourceA *= 2;
        }
        log.trace("Lock A released");

        synchronized (lockB) {
            log.trace("Lock B acquired");
            resourceB *= 2;
        }
        log.trace("Lock B released, exit");
    }
}
