/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s12;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock and ReentrantLock.
 * 
 * Define two resources (actually, doubles) and protect them by lock/unlock on the associated lock
 */
public class LockPlain {
    private Lock lockF = new ReentrantLock();
    private double resourceF = 0.0;

    private Lock lockG = new ReentrantLock();
    private double resourceG = 0.0;

    /**
     * Run a few threads concurrently on the two resources.
     * 
     * @param args not used
     * @throws InterruptedException when join in main is interrupted (should not happen)
     */
    public static void main(String[] args) throws InterruptedException {
        LockPlain lp = new LockPlain();

        Thread[] threads = { new Thread(lp::syncOnF, "F1"), new Thread(lp::syncOnG, "G1"),
                new Thread(lp::syncOnF, "F2"), new Thread(lp::syncOnG, "G2") };

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.printf("Resource F is %f%n", lp.resourceF);
        System.out.printf("Resource G is %f%n", lp.resourceG);
        System.out.println("Bye from " + Thread.currentThread().getName());
    }

    /**
     * For threads accessing resource F
     */
    public void syncOnF() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " needs the lock on F");

        try {
            lockF.lock();
            double value = aRiskyJob();
            System.out.printf("%s is adding %f to F%n", name, value);
            resourceF += value;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println(name + " unlock on F");
            lockF.unlock();
        }
    }

    /**
     * For threads accessing resource G
     */
    public void syncOnG() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " needs the lock on G");

        try {
            lockG.lock();
            double value = aRiskyJob();
            System.out.printf("%s is adding %f to G%n", name, value);
            resourceG += value;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println(name + " unlock on G");
            lockG.unlock();
        }
    }

    /**
     * A job that could go wrong
     * 
     * @throws IllegalStateException
     */
    private double aRiskyJob() {
        double result = Math.random();
        if (result > 0.5) {
            throw new IllegalStateException(Thread.currentThread().getName() + ", something bad happened");
        }
        return result;
    }
}
