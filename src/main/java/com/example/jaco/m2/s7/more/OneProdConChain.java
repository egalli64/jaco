/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s7.more;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m2.s5.more.Product;

/**
 * Lock and Condition
 * <p>
 * Chain of exchanges between a producer and a consumer. Two conditions are used
 * to let producer and consumer communicate about their state.
 */
public class OneProdConChain {
    private static final Logger log = LoggerFactory.getLogger(OneProdConChain.class);

    /** Number of products the producer is going to produce */
    private static final int PRODUCT_NR = 3;

    private Product product;
    private final Lock lock;
    private final Condition availability;
    private final Condition consumption;

    /**
     * Constructor
     */
    public OneProdConChain() {
        this.product = new Product();
        this.lock = new ReentrantLock();
        this.availability = lock.newCondition();
        this.consumption = lock.newCondition();
    }

    /**
     * For the producer thread
     * <p>
     * Produce the requested products, once a time, signaling its availability then
     * terminate
     */
    private void produce() {
        log.trace("Enter");

        for (int i = 0; i < PRODUCT_NR; i++) {
            lock.lock();
            try {
                log.trace("Lock acquired");
                while (!product.isConsumed()) {
                    log.trace("Wait for product consumption");
                    consumption.await();
                    log.trace("Consumption has been signaled");
                }

                product.produce(i, ThreadLocalRandom.current().nextInt(1, 7));
                System.out.println("Producer signals production of " + product);
                availability.signal();
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
     * it. Then consume it.
     */
    private void consume() {
        log.trace("Enter");
        String name = Thread.currentThread().getName();

        while (!Thread.currentThread().isInterrupted()) {
            lock.lock();
            log.trace("Lock acquired");
            try {
                while (!product.isProduced()) {
                    log.trace("Wait for product availability");
                    availability.await();
                    log.trace("Availablility has been signaled");
                }

                int value = product.consume();
                System.out.printf("%s signals consumption of product: %d\n", name, value);
                consumption.signal();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.trace("Exit caused by interrupt while waiting on production");
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
        OneProdConChain pc = new OneProdConChain();

        Thread consumer = new Thread(pc::consume, "C");
        consumer.start();

        Thread producer = new Thread(pc::produce, "P");
        producer.start();

        System.out.println("Setup done from main");

        producer.join();

        System.out.println("Producer is done, main terminates the consumer");

        consumer.interrupt();
        consumer.join();

        log.trace("Exit");
    }
}
