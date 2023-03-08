/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s12;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.DoubleStream;

/**
 * Lock and ReentrantLock.
 * 
 * Similar to LockTry, but using timed tryLock()
 */
public class LockTryWait {
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
        LockTryWait ltw = new LockTryWait();

        Thread[] threads = { new Thread(ltw::syncOnF, "F1"), new Thread(ltw::syncOnG, "G1"),
                new Thread(ltw::syncOnF, "F2"), new Thread(ltw::syncOnG, "G2") };

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.printf("Resource F is %f%n", ltw.resourceF);
        System.out.printf("Resource G is %f%n", ltw.resourceG);
        System.out.println("Bye from " + Thread.currentThread().getName());
    }

    /**
     * For threads accessing resource F
     * 
     * Let the tryLock wait enough time to reasonably acquire the lock
     */
    public void syncOnF() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " try the lock on F for some millis");

        boolean locked = false;
        try {
            locked = lockF.tryLock(50, TimeUnit.MILLISECONDS);
            if (locked) {
                double value = aLongishJob();
                System.out.printf("%s is adding %f to F%n", name, value);
                resourceF += value;
            }
        } catch (InterruptedException e) {
            System.out.println(name + " wait on lock unexpectedly interrupted");
        } finally {
            if (locked) {
                System.out.println(name + " unlock on F");
                lockF.unlock();
            } else {
                System.out.println(name + " could not get the lock in the specified time frame");
            }
        }
    }

    /**
     * For threads accessing resource G
     * 
     * Let the tryLock wait for a short time, so that we can expect non-availability
     */
    public void syncOnG() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " try the lock on G for a few millis");

        boolean locked = false;
        try {
            locked = lockG.tryLock(5, TimeUnit.MILLISECONDS);
            if (locked) {
                double value = aLongishJob();
                System.out.printf("%s is adding %f to G%n", name, value);
                resourceG += value;
            }
        } catch (InterruptedException e) {
            System.out.println(name + " wait on lock unexpectedly interrupted");
        } finally {
            if (locked) {
                System.out.println(name + " unlock on G");
                lockG.unlock();
            } else {
                System.out.println(name + " could not get the lock in the specified time frame");
            }
        }
    }

    /**
     * Just take some times running
     * 
     * @return a value
     */
    private double aLongishJob() {
        return DoubleStream.generate(() -> Math.cbrt(Math.random())).limit(100).sum();
    }
}
