/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m1.s08;

/**
 * Finer synchronization, using more locks.
 * 
 * !!! Assume this is legacy code, see Lock for modern implementation !!!
 */
public class SynchroOnObjects {
    private Object lockA = new Object();
    private int resourceA = 0;
    private Object lockB = new Object();
    private int resourceB = 0;

    /**
     * Eight working threads divided in four groups
     * <li>Synchronized on this (standard) for System.out
     * <li>Synchronized on lock "A" for resourceA
     * <li>Synchronized on lock "B" for resourceB
     * <li>Synchronized on lock "A" and then "B" for both resources
     * 
     * @param args not used
     */
    public static void main(String[] args) {
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
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new IllegalArgumentException(e);
            }
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
