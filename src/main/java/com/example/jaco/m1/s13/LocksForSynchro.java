/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s13;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Finer synchronization, using ReentrantLock.
 * 
 * Compare it to SynchroOnObjects
 */
public class LocksForSynchro {
    private Lock lockA = new ReentrantLock();
    private int resourceA = 0;
    private Lock lockB = new ReentrantLock();
    private int resourceB = 0;

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
        LocksForSynchro lfs = new LocksForSynchro();

        Thread[] threads = { //
                new Thread(lfs::syncThis, "This1"), new Thread(lfs::syncA, "A1"), //
                new Thread(lfs::syncB, "B1"), new Thread(lfs::syncAB, "AB1"), //
                new Thread(lfs::syncThis, "This2"), new Thread(lfs::syncA, "A2"), //
                new Thread(lfs::syncB, "B2"), new Thread(lfs::syncAB, "AB2") //
        };

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
        System.out.println("Bye from " + Thread.currentThread().getName());
    }

    /**
     * Synchronization for System.out
     * 
     * A and B are in read-only access, then there is no race condition on them
     */
    public synchronized void syncThis() {
        System.out.println(Thread.currentThread().getName() + " enter syncOnThis()");
        System.out.printf("%s A is %d, B is %d%n", Thread.currentThread().getName(), resourceA, resourceB);
        System.out.println(Thread.currentThread().getName() + " exit syncOnThis()");
    }

    /**
     * Synchronization for resourceA
     */
    public void syncA() {
        System.err.println(Thread.currentThread().getName() + " enter sync block on A");
        synchronized (lockA) {
            resourceA += 1;
        }
        System.err.println(Thread.currentThread().getName() + " exit sync block on A");
    }

    /**
     * Synchronization for resourceB
     */
    public void syncB() {
        System.err.println(Thread.currentThread().getName() + " enter sync block on B");
        synchronized (lockB) {
            resourceB += 1;
        }
        System.err.println(Thread.currentThread().getName() + " exit sync block on B");
    }

    /**
     * Synchronization for A and then B
     */
    public void syncAB() {
        System.err.println(Thread.currentThread().getName() + " enter sync block on A");
        synchronized (lockB) {
            resourceA *= 2;
        }
        System.err.println(Thread.currentThread().getName() + " exit sync block on A");

        System.err.println(Thread.currentThread().getName() + " enter sync block on B");
        synchronized (lockB) {
            resourceB *= 2;
        }
        System.err.println(Thread.currentThread().getName() + " exit sync block on B");
    }
}
