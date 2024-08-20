/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s3;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread communication - synchronized block as monitor: wait/notify
 * <p>
 * One Producer - One Consumer
 */
public class OneProdCon {
    private static final Logger log = LoggerFactory.getLogger(OneProdCon.class);

    private static final int PRODUCT_NOT_READY = 0;

    /** The resource shared between two threads */
    private int product = PRODUCT_NOT_READY;

    /**
     * The producer thread runs this method, that sets the shared resource.
     * <p>
     * Once the job is done it notifies the consumer thread about it.
     */
    private synchronized void producer() {
        log.trace("Enter");
        product = ThreadLocalRandom.current().nextInt(1, 7);
        System.out.printf("%s has produced %d%n", Thread.currentThread().getName(), product);
        // Since there is just one consumer, here notifyAll() would be an overkill
        notify();
        log.trace("Exit");
    }

    /**
     * The consumer thread runs this method.
     * 
     * It waits the producer to set the product, then consumes it.
     */
    private synchronized void consumer() {
        log.trace("Enter");
        String tName = Thread.currentThread().getName();
        try {
            System.out.println(tName + " requires a product");

            // Wait until the product is available
            while (product == PRODUCT_NOT_READY) {
                System.out.println(tName + " waits for the result");
                wait();
                System.out.println(tName + " wait has ended");
            }

            // It is safe to assume that now product is ready to be consumed
            System.out.printf("%s consumes %d%n", tName, product);
            product = PRODUCT_NOT_READY;
        } catch (InterruptedException e) {
            // In this simple case, it is not legal interrupting a consumer
            throw new IllegalStateException(e);
        }
        log.trace("Exit");
    }

    /**
     * Start consumer and producer, then join them.
     * 
     * @param args not used
     * @throws InterruptedException when a join is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        OneProdCon wn = new OneProdCon();

        System.out.println("Create and start consumer and producer");
        Thread[] threads = { new Thread(wn::consumer, "consumer"), new Thread(wn::producer, "producer") };
        Arrays.stream(threads).forEach(Thread::start);

        System.out.println("Nothing else to do in main");
        for (Thread t : threads) {
            t.join();
        }

        log.trace("Exit");
    }
}
