/*
 * Introduction to Java Concurrency
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
 * <p>
 * A simple exchange between a producer and a few consumers
 */
public class OneProdManyCons {
    private static final Logger log = LoggerFactory.getLogger(OneProdManyCons.class);

    private double product;
    /** true when the product is not valid (anymore) */
    private boolean consumed;
    private Lock lock;
    private Condition availability;
    private Condition consumption;

    /**
     * Constructor
     */
    public OneProdManyCons() {
        this.product = 0.0;
        this.consumed = true;
        this.lock = new ReentrantLock();
        this.availability = lock.newCondition();
        this.consumption = lock.newCondition();
    }

    /**
     * For the producer thread, loop until interrupted.
     * <p>
     * Produce a product, signal all, then wait consumption before produce again.
     */
    private void produce() {
        log.trace("Enter");

        while (!Thread.currentThread().isInterrupted()) {
            lock.lock();
            log.trace("Lock acquired");
            try {
                while (!consumed) {
                    log.trace("Wait for product consumption");
                    consumption.await();
                    log.trace("Consumption has been signaled");
                }

                // Since a unique producer is expected, no need of ThreadLocalRandom
                product = Math.random();
                consumed = false;
                System.out.printf("Producer signals availability of %f%n", product);
                availability.signalAll();
            } catch (InterruptedException ex) {
                log.trace("Exit caused by interrupt while waiting on consumption");
                return;
            } finally {
                lock.unlock();
                log.trace("Lock released");
            }
        }
        log.trace("Exit");
    }

    /**
     * For the consumer threads.
     * <p>
     * Wait for a not-already-consumed product. Signal that, and then terminate.
     */
    private void consume() {
        log.trace("Enter");
        String name = Thread.currentThread().getName();

        lock.lock();
        log.trace("Lock acquired");
        try {
            while (consumed) {
                log.trace("Wait for product availability");
                availability.await();
                log.trace("Availablility has been signaled");
            }

            System.out.printf("%s signals consumption of %f%n", name, product);
            consumed = true;
            consumption.signal();
        } catch (InterruptedException ex) {
            log.warn("Exit with an unexpected interruption when waiting on availability", ex);
            return;
        } finally {
            lock.unlock();
            log.trace("Lock released and exit");
        }
    }

    /**
     * Create and start the producer, then create the consumers.
     * <p>
     * Join on them, and then terminate the producer.
     * 
     * @param args not used
     * @throws InterruptedException when interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        OneProdManyCons pc = new OneProdManyCons();

        Thread producer = new Thread(pc::produce, "P");
        producer.start();

        Thread[] consumers = { //
                new Thread(pc::consume, "C1"), //
                new Thread(pc::consume, "C2"), //
                new Thread(pc::consume, "C3") //
        };

        Arrays.stream(consumers).forEach(Thread::start);
        for (Thread consumer : consumers) {
            consumer.join();
        }

        System.out.println("No more consumer, interrupting producer");
        producer.interrupt();
        producer.join();

        log.trace("Exit");
    }
}
