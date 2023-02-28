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
     * Six working threads divided in three groups
     * <li>Synchronized on this (standard) for System.out
     * <li>Synchronized on lock "A" for resourceA
     * <li>Synchronized on lock "B" for resourceB
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        SynchroOnObjects soo = new SynchroOnObjects();

        Thread[] threads = { //
                new Thread(soo::syncOnThis, "This1"), //
                new Thread(soo::syncOnA, "A1"), //
                new Thread(soo::syncOnB, "B1"), //
                new Thread(soo::syncOnThis, "This2"), //
                new Thread(soo::syncOnA, "A2"), //
                new Thread(soo::syncOnB, "B2") //
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

    public synchronized void syncOnThis() {
        System.out.println(Thread.currentThread().getName() + " enter syncOnThis()");
        System.out.printf("%s A is %d, B is %d%n", Thread.currentThread().getName(), resourceA, resourceB);
        System.out.println(Thread.currentThread().getName() + " exit syncOnThis()");
    }

    public void syncOnA() {
        synchronized (lockA) {
            System.err.println(Thread.currentThread().getName() + " enter sync block on A");
            resourceA += 1;
            System.err.println(Thread.currentThread().getName() + " exit sync block on A");
        }
    }

    public void syncOnB() {
        synchronized (lockB) {
            System.err.println(Thread.currentThread().getName() + " enter sync block on G");
            resourceB += 1;
            System.err.println(Thread.currentThread().getName() + " exit sync block on G");
        }
    }
}
