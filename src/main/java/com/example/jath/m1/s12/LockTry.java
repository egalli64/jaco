package com.example.jath.m1.s12;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTry {
    private Lock lockF = new ReentrantLock();
    private volatile double resourceF = 0.0;

    private Lock lockG = new ReentrantLock();
    private volatile double resourceG = 0.0;

    public static void main(String[] args) {
        LockTry lt = new LockTry();

        Thread[] threads = { //
                new Thread(lt::syncOnF, "F1"), //
                new Thread(lt::syncOnG, "G1"), //
                new Thread(lt::syncOnF, "F2"), //
                new Thread(lt::syncOnG, "G2") //
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

        System.out.printf("Resource F is %f%n", lt.resourceF);
        System.out.printf("Resource F is %f%n", lt.resourceG);
        System.out.println("Bye from " + Thread.currentThread().getName());
    }

    public void syncOnF() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " try the lock on F");

        if (lockF.tryLock()) {
            try {
                double value = aRiskyJob();
                System.out.printf("%s is adding %f to F%n", name, value);
                resourceF += value;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                System.out.println(name + " unlock on F");
                lockF.unlock();
            }
        } else {
            System.out.println(name + " skip the job, F is already locked");
        }
    }

    public void syncOnG() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " try the lock on G");

        if (lockG.tryLock()) {
            try {
                double value = aRiskyJob();
                System.out.printf("%s is adding %f to G%n", name, value);
                resourceG += value;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                System.out.println(name + " unlock on G");
                lockG.unlock();
            }
        } else {
            System.out.println(name + " skip the job, G is already locked");
        }
    }

    /**
     * A job that could go wrong
     * 
     * @throws IllegalStateException
     */
    private double aRiskyJob() {
        double result = Math.random();
        if (result > 0.7) {
            throw new IllegalStateException(Thread.currentThread().getName() + ", something bad happened");
        }
        return result;
    }
}
