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

import com.example.jaco.m2.s5.Product;

/**
 * Lock and Condition
 * <p>
 * A single exchange between a producer and a consumer
 */
public class OneProdCon {
    private static final Logger log = LoggerFactory.getLogger(OneProdCon.class);

    private Product product;
    private final Lock lock;
    private final Condition availability;

    /**
     * Constructor
     */
    public OneProdCon() {
        this.product = new Product();
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
            product.produce(0, ThreadLocalRandom.current().nextInt(1, 7));

            System.out.println(Thread.currentThread().getName() + " signal availability of " + product);
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
            while (!product.isProduced()) {
                log.trace("Waiting product availability");
                availability.await();
                if (!product.isProduced()) {
                    log.warn("Spurious wakeup detected, keep waiting");
                } else {
                    log.trace("Signaled product availability");
                }
            }

            int value = product.consume();
            System.out.println(Thread.currentThread().getName() + " has consumed product: " + value);
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
