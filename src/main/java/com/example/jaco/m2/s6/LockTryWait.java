/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s6;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.DoubleStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lock and ReentrantLock.
 * <p>
 * Compare it to LockTry, here tryLock() is used
 */
public class LockTryWait {
    private static final Logger log = LoggerFactory.getLogger(LockTryWait.class);

    private final Lock lockF;
    private double resourceF;

    private final Lock lockG;
    private double resourceG;

    /**
     * Constructor, initialize locks and resources
     */
    public LockTryWait() {
        this.lockF = new ReentrantLock();
        this.resourceF = 0.0;

        this.lockG = new ReentrantLock();
        this.resourceG = 0.0;
    }

    /**
     * Run a few threads concurrently on the two resources.
     * 
     * @param args not used
     * @throws InterruptedException in case of unexpected interrupted join
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        LockTryWait ltw = new LockTryWait();

        Thread[] threads = { //
                new Thread(ltw::syncOnF, "F1"), //
                new Thread(ltw::syncOnG, "G1"), //
                new Thread(ltw::syncOnF, "F2"), //
                new Thread(ltw::syncOnG, "G2") //
        };

        Arrays.stream(threads).forEach(Thread::start);
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Resource F is " + ltw.resourceF);
        System.out.println("Resource G is " + ltw.resourceG);
        log.trace("Exit");
    }

    /**
     * For threads accessing resource F
     * <p>
     * Let the tryLock wait enough time to reasonably acquire the lock
     */
    public void syncOnF() {
        log.trace("Enter and try-lock (timed) on F");
        String name = Thread.currentThread().getName();

        boolean locked = false;
        try {
            locked = lockF.tryLock(50, TimeUnit.MILLISECONDS);
            if (locked) {
                double value = aLongishJob();
                System.out.printf("%s is adding %f to F\n", name, value);
                resourceF += value;
            }
        } catch (InterruptedException ex) {
            log.warn("wait on lock unexpectedly interrupted ", ex);
        } finally {
            if (locked) {
                log.trace("Unlock F then exit");
                lockF.unlock();
            } else {
                log.warn("Exit without doing anything, lock was not available in the given time frame");
            }
        }
    }

    /**
     * For threads accessing resource G
     * <p>
     * Let the tryLock wait for a short time, so that we can expect non-availability
     */
    public void syncOnG() {
        log.trace("Enter and try-lock (very shortly timed) on G");
        String name = Thread.currentThread().getName();

        boolean locked = false;
        try {
            locked = lockG.tryLock(5, TimeUnit.MILLISECONDS);
            if (locked) {
                double value = aLongishJob();
                System.out.printf("%s is adding %f to G\n", name, value);
                resourceG += value;
            }
        } catch (InterruptedException ex) {
            log.warn("wait on lock unexpectedly interrupted ", ex);
        } finally {
            if (locked) {
                log.trace("Unlock G then exit");
                lockG.unlock();
            } else {
                log.warn("Exit without doing anything, lock was not available in the given time frame");
            }
        }
    }

    /**
     * Just take some times running
     * 
     * @return a value
     */
    private double aLongishJob() {
        log.trace("Enter");
        return DoubleStream.generate(() -> Math.cbrt(Math.random())).limit(100).sum();
    }
}
