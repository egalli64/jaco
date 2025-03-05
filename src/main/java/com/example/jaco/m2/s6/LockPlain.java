/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s6;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lock and ReentrantLock.
 * <p>
 * Two resources (double primitives) protected by locks, by lock() and unlock()
 */
public class LockPlain {
    private static final Logger log = LoggerFactory.getLogger(LockPlain.class);

    private final Lock lockF;
    private double resourceF;

    private final Lock lockG;
    private double resourceG;

    /**
     * Constructor, initialize locks and resources
     */
    public LockPlain() {
        this.lockF = new ReentrantLock();
        this.resourceF = 0.0;

        this.lockG = new ReentrantLock();
        this.resourceG = 0.0;
    }

    /**
     * Run a few threads concurrently on the two resources
     * 
     * @param args not used
     * @throws InterruptedException in case of unexpected interrupted join
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");

        LockPlain lp = new LockPlain();

        Thread[] threads = { //
                new Thread(lp::syncOnF, "F1"), //
                new Thread(lp::syncOnG, "G1"), //
                new Thread(lp::syncOnF, "F2"), //
                new Thread(lp::syncOnG, "G2") //
        };

        Arrays.stream(threads).forEach(Thread::start);
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Resource F is " + lp.resourceF);
        System.out.println("Resource G is " + lp.resourceG);

        log.trace("Exit");
    }

    /**
     * Lock and modify resource F
     */
    public void syncOnF() {
        String name = Thread.currentThread().getName();
        lockF.lock();
        log.trace("Lock acquired on F");
        
        try {
            double value = aRiskyJob();
            System.out.printf("%s is adding %f to F\n", name, value);
            resourceF += value;
        } catch (Exception e) {
            System.out.println(name + " not adding to F: " + e.getMessage());
        } finally {
            log.trace("Unlock F then exit");
            lockF.unlock();
        }
    }

    /**
     * Lock and modify resource G
     */
    public void syncOnG() {
        String name = Thread.currentThread().getName();
        lockG.lock();
        log.trace("Lock acquired on G");
        
        try {
            double value = aRiskyJob();
            System.out.printf("%s is adding %f to G\n", name, value);
            resourceG += value;
        } catch (Exception e) {
            System.out.println(name + " not adding to G: " + e.getMessage());
        } finally {
            log.trace("Unlock G then exit");
            lockG.unlock();
        }
    }

    /**
     * A job that could fail randomly
     * 
     * @throws IllegalStateException if an invalid value is generated 
     */
    private double aRiskyJob() {
        log.trace("Enter");
        try {
            double result = ThreadLocalRandom.current().nextDouble();
            if (result > 0.5) {
                throw new IllegalStateException("wrong value detected");
            }

            return result;
        } finally {
            log.trace("Exit");
        }
    }
}
