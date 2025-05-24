/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s7;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lock and Condition
 * <p>
 * Chain of exchanges between a producer and a consumer. Two conditions are used
 * to let producer and consumer communicate about their state.
 */
public class AwaitSignalChain {
    private static final Logger log = LoggerFactory.getLogger(AwaitSignalChain.class);

    /** Number of products the producer is going to produce */
    private static final int PRODUCT_NR = 3;

    /** true when the product is ready for consumption */
    private boolean ready;
    private int product;
    private final Lock lock;
    private final Condition availability;
    private final Condition consumption;

    /**
     * Constructor
     */
    public AwaitSignalChain() {
        this.ready = false;
        this.lock = new ReentrantLock();
        this.availability = lock.newCondition();
        this.consumption = lock.newCondition();
    }

    /**
     * For the producer thread
     * <p>
     * Produce the requested products, once a time, signaling its availability
     */
    private void produce() {
        log.trace("Enter");

        for (int i = 0; i < PRODUCT_NR; i++) {
            lock.lock();
            try {
                log.trace("Lock acquired");
                while (ready) {
                    log.trace("Await for product consumption");
                    consumption.await();
                    log.trace("Consumption has been signaled");
                }

                product = ThreadLocalRandom.current().nextInt(1, 7);
                System.out.println("Producer signal availability of product " + product);
                // actually, signal() would suffice, since only one thread is awaiting
                availability.signalAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.warn("Exit with an unexpected interruption when waiting on consumption", ex);
                return;
            } finally {
                lock.unlock();
                log.trace("Lock released");
            }
        }
        log.trace("Exit");
    }

    /**
     * For the consumer thread
     * <p>
     * Acquire the lock, if the product is not ready, await() on the condition for
     * it. Then consume it. Loop indefinitely, expect an interrupt to terminate.
     */
    private void consume() {
        log.trace("Enter");
        while (!Thread.currentThread().isInterrupted()) {
            lock.lock();
            log.trace("Lock acquired");
            try {
                while (!ready) {
                    log.trace("Await for product availability");
                    availability.await();
                    log.trace("Availablility has been signaled");
                }

                System.out.printf("Product %d has been consumed\n", product);
                ready = false;
                // for robustness, actually signal() would suffice
                consumption.signalAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.trace("Exit caused by interrupt while awaiting");
                return;
            } finally {
                lock.unlock();
                log.trace("Lock released");
            }
        }
        log.trace("Exit");
    }

    /**
     * Create and start a consumer thread, that hangs, waiting for the producer.
     * <p>
     * Then create and start a producer thread, that signals its production to the
     * consumer.
     * 
     * @param args not used
     * @throws InterruptedException when a join is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        AwaitSignalChain pc = new AwaitSignalChain();

        Thread consumer = new Thread(pc::consume, "Consumer");
        consumer.start();

        Thread producer = new Thread(pc::produce, "Producer");
        producer.start();

        System.out.println("Setup done from main");

        producer.join();

        System.out.println("Producer is done, shutdown the consumer");

        consumer.interrupt();
        consumer.join();

        log.trace("Exit");
    }
}
