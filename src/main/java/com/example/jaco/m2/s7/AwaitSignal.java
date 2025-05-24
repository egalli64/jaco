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
 * Lock and Condition: await and signal
 * <p>
 * A single exchange between a producer and a consumer
 */
public class AwaitSignal {
    private static final Logger log = LoggerFactory.getLogger(AwaitSignal.class);

    private boolean ready;
    private int product;
    private final Lock lock;
    private final Condition availability;

    /**
     * Constructor
     */
    public AwaitSignal() {
        this.ready = false;
        this.lock = new ReentrantLock();
        this.availability = lock.newCondition();
    }

    /**
     * For the producer thread
     * <p>
     * Produce a single product, signal its availability, then terminate
     */
    private void produce() {
        log.trace("Enter");

        lock.lock();
        try {
            log.trace("Lock acquired");
            product = ThreadLocalRandom.current().nextInt(1, 7);
            ready = true;

            System.out.println("Producer signal availability of product " + product);
            // here signalAll is not necessary (there is just one thread awaiting)
            // usually, for robustness, it is used anyway
            availability.signal();
        } finally {
            lock.unlock();
            log.trace("Lock released, exit");
        }
    }

    /**
     * For the consumer thread
     * <p>
     * Wait for a product, consume it, then terminate
     */
    private void consume() {
        log.trace("Enter");

        lock.lock();
        try {
            log.trace("Lock acquired");
            while (!ready) {
                log.trace("Waiting product availability");
                availability.await();
                if (!ready) {
                    log.warn("Spurious wakeup detected, keep waiting");
                } else {
                    log.trace("Signaled product availability");
                }
            }

            System.out.println("Consumer has consumed product " + product);
            // just for robustness, no real need of resetting the ready flag in this example
            ready = false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Consumer interrupted while waiting", e);
        } finally {
            lock.unlock();
            log.trace("Lock released, exit");
        }
    }

    /**
     * Create and start producer and consumer. Let them play, then terminate
     * <p>
     * Notice that the execution order is not deterministic
     * 
     * @param args not used
     * @throws InterruptedException if interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        AwaitSignal pc = new AwaitSignal();

        Thread[] threads = { new Thread(pc::consume, "Consumer"), new Thread(pc::produce, "Producer") };
        Arrays.stream(threads).forEach(Thread::start);

        System.out.println("Main waits");
        for (Thread t : threads) {
            t.join();
        }

        log.trace("Exit");
    }
}
