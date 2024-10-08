/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s7;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lock and Condition
 * <p>
 * A single exchange between a producer and a consumer
 */
public class OneProdCon {
    private static final Logger log = LoggerFactory.getLogger(OneProdCon.class);

    private int product;
    private boolean produced;
    private final Lock lock;
    private final Condition availability;

    /**
     * Constructor
     */
    public OneProdCon() {
        this.produced = false;
        this.lock = new ReentrantLock();
        this.availability = lock.newCondition();
    }

    /**
     * For the producer thread.
     * <p>
     * Produce a single product, signal its availability, then terminate.
     */
    private void produce() {
        log.trace("Enter");

        lock.lock();
        try {
            log.trace("Lock acquired");
            product = ThreadLocalRandom.current().nextInt(1, 7);
            produced = true;

            System.out.println(Thread.currentThread().getName() + " signal availability of " + product);
            availability.signal();
        } finally {
            lock.unlock();
            log.trace("Lock released, exit");
        }
    }

    /**
     * For the consumer thread.
     * <p>
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

            System.out.println(Thread.currentThread().getName() + " consumes " + product);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            lock.unlock();
            log.trace("Lock released, exit");
        }
    }

    /**
     * Create and start producer and consumer. Let them play, then terminate.
     * <p>
     * Notice that the execution order is not deterministic.
     * 
     * @param args not used
     * @throws InterruptedException when interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        OneProdCon pc = new OneProdCon();

        Thread[] threads = { //
                new Thread(pc::consume, "C"), //
                new Thread(pc::produce, "P") //
        };

        Arrays.stream(threads).forEach(Thread::start);

        System.out.println("Main waits");
        for (Thread t : threads) {
            t.join();
        }

        log.trace("Exit");
    }
}
