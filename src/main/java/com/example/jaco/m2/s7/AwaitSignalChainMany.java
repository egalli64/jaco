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

import com.example.jaco.FakeTasks;

/**
 * Lock and Condition
 * <p>
 * Chain of exchanges between a producer and many consumers. Two conditions are
 * used to let producer and consumer communicate about their state.
 * <p>
 * The producer produces the products, each product is meant to be consumed by a
 * single consumer. No assumption is done on which consumer is going to consume
 * a specific product. Potentially, a single consumer could consume all
 * products.
 */
public class AwaitSignalChainMany {
    private static final Logger log = LoggerFactory.getLogger(AwaitSignalChainMany.class);

    /** Number of products the producer is going to produce */
    private static final int PRODUCT_NR = 10;

    private boolean ready;
    private int product;
    private final Lock lock;
    private final Condition availability;
    private final Condition consumption;

    /**
     * Constructor, flag the product not ready to consumption
     */
    public AwaitSignalChainMany() {
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
                ready = true;

                System.out.println("Producer signal availability of product " + product);
                availability.signalAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.warn("Exit with an unexpected interruption when awaiting", ex);
                return;
            } finally {
                lock.unlock();
                log.trace("Lock released");
            }
        }
        log.trace("Exit");
    }

    /**
     * For each consumer thread
     * <p>
     * Acquire the lock, if the product is not ready, await() on the condition for
     * it. Then consume it. Loop until interrupted.
     */
    private void consume() {
        log.trace("Enter");
        int count = 1;

        while (!Thread.currentThread().isInterrupted()) {
            // keep track if the product is actually consumed
            boolean consumed = false;
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
                consumed = true;
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.trace("Interrupt while about consuming my #{} product", count);
            } finally {
                // only if the consumption is actually done, signal it
                if (consumed) {
                    consumption.signalAll();
                }
                lock.unlock();
                log.trace("Lock released");

                // simulate use of data - outside the critical area
                if (consumed) {
                    FakeTasks.calc(1_000 * count);
                    count += 1;
                }
            }
        }
        log.trace("Exit");
    }

    /**
     * Create and start the consumer threads, then a producer thread, that signals
     * its production to the consumers. When the production ends, the consumers are
     * interrupted.
     * 
     * @param args not used
     * @throws InterruptedException when a join is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        AwaitSignalChainMany pc = new AwaitSignalChainMany();

        Thread[] consumers = { //
                new Thread(pc::consume, "Consumer1"), //
                new Thread(pc::consume, "Consumer2"), //
                new Thread(pc::consume, "Consumer3") //
        };
        Arrays.stream(consumers).forEach(Thread::start);

        Thread producer = new Thread(pc::produce, "Producer");
        producer.start();

        System.out.println("Setup done from main");

        producer.join();

        System.out.println("Producer has terminated, shutdown the consumers");
        Arrays.stream(consumers).forEach(Thread::interrupt);

        for (Thread consumer : consumers) {
            consumer.join();
        }

        log.trace("Exit");
    }
}
