/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s5;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread communication - synchronized block as monitor: wait/notify
 * <p>
 * Chain of exchanges between a single producer and single consumer
 */
public class OneProdConChain {
    private static final Logger log = LoggerFactory.getLogger(OneProdConChain.class);

    /** Number of product to be generated */
    private static final int PRODUCT_NR = 3;

    /** The resource shared between two threads */
    private Product product = new Product();

    /**
     * The producer thread runs this method to set the shared resource
     * <p>
     * It will produce PRODUCT_NR products before terminating. After each production
     * notifies the (unique) consumer and waits till the consumer notifies the
     * consumption.
     */
    private synchronized void producer() {
        log.trace("Enter");
        final String tName = Thread.currentThread().getName();

        try {
            for (int i = 0; i < PRODUCT_NR; i++) {
                product.produce(i, ThreadLocalRandom.current().nextInt(1, 7));
                System.out.printf("%s has produced %s\n", tName, product);

                // there is just one consumer, no need to notifyAll()
                notify();

                // no need to wait after the last product has been produced
                if (i < PRODUCT_NR - 1) {
                    System.out.println(tName + " waits the product to be consumed");
                    wait();
                }
            }
        } catch (InterruptedException e) {
            // in this simple example, it is not legal interrupting a consumer
            log.warn("Producer interrupted while waiting the product to be consumed");
            throw new IllegalStateException(e);
        }

        log.trace("Exit");
    }

    /**
     * The consumer thread runs this method
     * <p>
     * It waits for the producer to set the product before consuming it. It loops
     * until it consumes PRODUCT_NR products
     */
    private synchronized void consumer() {
        log.trace("Enter");
        final String tName = Thread.currentThread().getName();

        try {
            for (int i = 0; i < PRODUCT_NR; i++) {
                System.out.println(tName + " requires a product");

                // wait until the product is available - loop to avoid a spurious wake-up
                while (!product.isProduced()) {
                    System.out.println(tName + " waits a product");
                    wait();
                    System.out.println(tName + " wait has ended");
                }

                // it is safe to assume that now product is ready to be consumed
                System.out.printf("%s consumes %s\n", tName, product.consume());

                // notify producer to produce next product
                notify();
            }
        } catch (InterruptedException e) {
            // in this simple example, it is not legal interrupting a consumer
            log.warn("Consumer interrupted while waiting the product to be produced");
            throw new IllegalStateException(e);
        }
        log.trace("Exit");
    }

    /**
     * Start consumer and producer, then join on them.
     * 
     * @param args not used
     * @throws InterruptedException  join interrupted
     * @throws IllegalStateException wait interrupted in producer or consumer
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        OneProdConChain pc = new OneProdConChain();

        System.out.println("Create and start consumer and producer");
        Thread[] threads = { new Thread(pc::producer, "producer"), new Thread(pc::consumer, "consumer") };
        Arrays.stream(threads).forEach(Thread::start);

        System.out.println("Nothing else to do in main");
        for (Thread t : threads) {
            t.join();
        }

        log.trace("Exit");
    }
}
