/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s7.more;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m2.s5.more.Product;

/**
 * Fair Lock and Condition
 * <p>
 * A simple exchange between a producer and a few consumers
 */
public class OneProdManyCons {
    private static final Logger log = LoggerFactory.getLogger(OneProdManyCons.class);

    /** The shared resource */
    private Product product;
    private final Lock lock;
    /** signaled when a product is ready for consumption */
    private final Condition available;
    /** signaled when a product has been consumed */
    private final Condition consumed;

    /**
     * Constructor
     */
    public OneProdManyCons() {
        this.product = new Product();
        this.lock = new ReentrantLock(true);
        this.available = lock.newCondition();
        this.consumed = lock.newCondition();
    }

    /**
     * For the producer thread, loop until interrupted
     * <p>
     * Produce a product, signal all, then wait consumption before produce again.
     */
    private void produce() {
        log.trace("Enter");
        int pid = 1;

        while (!Thread.currentThread().isInterrupted()) {
            lock.lock();
            log.trace("Lock acquired");
            try {
                while (!product.isConsumed()) {
                    log.trace("Wait for product consumption");
                    consumed.await();
                    log.trace("Consumption has been signaled");
                }

                product.produce(pid++, ThreadLocalRandom.current().nextInt(1, 7));
                System.out.println("Producer signals availability of " + product);
                available.signalAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.warn("Exit caused by interrupt while waiting on consumption");
            } finally {
                lock.unlock();
                log.trace("Lock released");
            }
        }

        if (!product.isConsumed()) {
            System.out.printf("Product %s was still pending\n", product);
        }
        log.trace("Exit");
    }

    /**
     * For the consumer threads
     * <p>
     * Wait for a not-already-consumed product. Signal that, and then terminate
     */
    private void consume() {
        log.trace("Enter");
        String name = Thread.currentThread().getName();

        lock.lock();
        log.trace("Lock acquired");
        try {
            while (!product.isProduced()) {
                log.trace("Wait for product availability");
                available.await();
                log.trace("Availablility has been signaled");
            }

            int value = product.consume();
            System.out.println(name + " has consumed a product: " + value);
            consumed.signal();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.warn("Exit with an unexpected interruption when waiting on availability", ex);
        } finally {
            lock.unlock();
            log.trace("Lock released and exit");
        }
    }

    /**
     * Create and start the producer, then create the consumers
     * <p>
     * Join on them, and then terminate the producer
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
