/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s13;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lock and Condition
 * 
 * A single exchange between a producer and a consumer
 */
public class OneProdCon {
    private static final Logger log = LoggerFactory.getLogger(OneProdCon.class);

    private double product;
    private boolean produced;
    private Lock lock;
    private Condition availability;

    /**
     * Constructor
     */
    public OneProdCon() {
        this.product = 0.0;
        this.produced = false;
        this.lock = new ReentrantLock();
        this.availability = lock.newCondition();
    }

    /**
     * For the producer thread.
     * 
     * Produce a single product, signal its availability, then terminate.
     */
    private void produce() {
        log.trace("Enter");

        lock.lock();
        try {
            log.trace("Lock acquired");
            product = Math.random();
            produced = true;

            System.out.printf("%s signal availability of %f%n", Thread.currentThread().getName(), product);
            availability.signal();
        } finally {
            lock.unlock();
            log.trace("Lock released, exit");
        }
    }

    /**
     * For the consumer thread.
     * 
     * Wait for a product, consume it, then terminate.
     */
    private void consume() {
        log.trace("Enter");

        lock.lock();
        try {
            log.trace("Lock acquired");
            while (!produced) {
                log.trace("Waiting availability");
                availability.await();
                log.trace("Signaled availability");
            }

            System.out.printf("%s consumes %f%n", Thread.currentThread().getName(), product);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            lock.unlock();
            log.trace("Lock released, exit");
        }
    }

    /**
     * Create and start producer and consumer. Let them play, then terminate.
     *
     * Notice that the timing is not deterministic.
     * 
     * @param args not used
     * @throws InterruptedException when interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        OneProdCon pc = new OneProdCon();

        Thread[] threads = { new Thread(pc::consume, "C"), new Thread(pc::produce, "P") };

        Arrays.stream(threads).forEach(Thread::start);

        System.out.println("Main waits");

        for (Thread t : threads) {
            t.join();
        }

        log.trace("Exit");
    }
}
