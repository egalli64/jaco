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
 * Lock and ReentrantLock
 * <p>
 * Compare it to LockPlain, here tryLock() is used
 */
public class LockTry {
    private static final Logger log = LoggerFactory.getLogger(LockTry.class);

    private final Lock lockF;
    private double resourceF;

    private final Lock lockG;
    private double resourceG;

    /**
     * Constructor, initialize locks and resources
     */
    public LockTry() {
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
        LockTry lt = new LockTry();

        Thread[] threads = { //
                new Thread(lt::syncOnF, "F1"), //
                new Thread(lt::syncOnG, "G1"), //
                new Thread(lt::syncOnF, "F2"), //
                new Thread(lt::syncOnG, "G2") //
        };

        Arrays.stream(threads).forEach(Thread::start);
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Resource F is " + lt.resourceF);
        System.out.println("Resource G is " + lt.resourceG);
        log.trace("Exit");
    }

    /**
     * Lock and modify resource F
     */
    public void syncOnF() {
        log.trace("Enter and try-lock on F");
        String name = Thread.currentThread().getName();

        // Usually tryLock() is invoked with a timeout, waiting for a short period
        // before giving up. Here we want to see the not-acquiring behavior more often
        // than usually expected in real code
        if (lockF.tryLock()) {
            try {
                double value = aRiskyJob();
                System.out.printf("%s is adding %f to F\n", name, value);
                resourceF += value;
            } catch (Exception e) {
                System.out.printf(name + " not adding to F: " + e.getMessage());
            } finally {
                log.trace("Unlock F then exit");
                lockF.unlock();
            }
        } else {
            log.warn("Exit without doing anything, lock was not available");
        }
    }

    /**
     * Lock and modify resource G
     */
    public void syncOnG() {
        log.trace("Enter and try-lock on G");
        String name = Thread.currentThread().getName();

        if (lockG.tryLock()) {
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
        } else {
            log.warn("Exit without doing anything, lock was not available");
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
            if (result > 0.7) {
                throw new IllegalStateException("wrong value detected");
            }

            return result;
        } finally {
            log.trace("Exit");
        }
    }
}
