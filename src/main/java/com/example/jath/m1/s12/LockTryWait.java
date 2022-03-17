package com.example.jath.m1.s12;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTryWait {
    private Lock lockF = new ReentrantLock();
    private volatile double resourceF = 0.0;

    private Lock lockG = new ReentrantLock();
    private volatile double resourceG = 0.0;

    public static void main(String[] args) {
        LockTryWait ltw = new LockTryWait();

        Thread[] threads = { //
                new Thread(ltw::syncOnF, "F1"), //
                new Thread(ltw::syncOnG, "G1"), //
                new Thread(ltw::syncOnF, "F2"), //
                new Thread(ltw::syncOnG, "G2") //
        };

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.err.println("No interruption expected here - " + e.getMessage());
                throw new IllegalStateException(e);
            }
        }

        System.out.printf("Resource F is %f%n", ltw.resourceF);
        System.out.printf("Resource G is %f%n", ltw.resourceG);
        System.out.println("Bye from " + Thread.currentThread().getName());
    }

    public void syncOnF() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " try the lock on F for a few millis");

        boolean locked = false;
        try {
            locked = lockF.tryLock(30, TimeUnit.MILLISECONDS);
            if (locked) {
                double value = aLongishJob();
                System.out.printf("%s is adding %f to F%n", name, value);
                resourceF += value;
            } else {
                System.out.println(name + " could not get the lock in the specified time frame");
            }
        } catch (InterruptedException e) {
            System.out.println(name + " wait on lock unexpectedly interrupted");
        } finally {
            if (locked) {
                System.out.println(name + " unlock on F");
                lockF.unlock();
            }
        }
    }

    public void syncOnG() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " try the lock on G for a few micros");

        boolean locked = false;
        try {
            locked = lockG.tryLock(30, TimeUnit.MICROSECONDS);
            if (locked) {
                double value = aLongishJob();
                System.out.printf("%s is adding %f to G%n", name, value);
                resourceG += value;
            } else {
                System.out.println(name + " could not get the lock in the specified time frame");
            }
        } catch (InterruptedException e) {
            System.out.println(name + " wait on lock unexpectedly interrupted");
        } finally {
            if (locked) {
                System.out.println(name + " unlock on G");
                lockG.unlock();
            }
        }
    }

    private double aLongishJob() {
        double result = 0.0;
        for (int i = 0; i < 100; i++) {
            result += Math.cbrt(Math.random());
        }
        return result;
    }
}
