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
 * One Producer - Many Consumers, each consumer consumes a product and
 * terminates
 */
public class OneProdManyCons {
    private static final Logger log = LoggerFactory.getLogger(OneProdCon.class);
    private static final int PRODUCT_NOT_READY = 0;

    /** The resource shared between threads */
    private int product = PRODUCT_NOT_READY;

    /**
     * Utility method for demonstration purposes
     */
    private static synchronized void checkThreadStates() {
        log.trace("Enter");
        Thread[] ts = new Thread[6];
        // Thread::enumerate() should be used only for debugging and monitoring purposes
        int count = Thread.enumerate(ts);
        for (int i = 0; i < count; i++) {
            System.out.printf("%s is %s\n", ts[i].getName(), ts[i].getState());
        }
        log.trace("Exit");
    }

    /**
     * The producer thread runs this method, that sets the shared resource.
     * <ul>
     * <li>The production has to be terminated by interrupt.
     * <li>After the production the producer notify all to consume it.
     * <li>If there is a non-consumed product, the producer wait for its consumption
     */
    private synchronized void produce() {
        log.trace("Enter");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Producer is ready ... ");
                product = ThreadLocalRandom.current().nextInt(1, 7);
                System.out.printf("Producer generated %d and it is about to notify all for it\n", product);

                checkThreadStates();
                notifyAll();
                while (product != PRODUCT_NOT_READY) {
                    System.out.println("Producer waits");
                    wait();
                    System.out.println("Producer wait has ended");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Producer wait has been interrupted");
            Thread.currentThread().interrupt();
        } finally {
            if (product != PRODUCT_NOT_READY) {
                System.out.println("Last product has not been consumed");
            }
            log.trace("Exit");
        }
    }

    /**
     * Each consumer thread runs this method.
     * <p>
     * It waits the producer to set the product, then consumes it.
     * 
     * @throws IllegalStateException if the thread is interrupted
     */
    private synchronized void consume() {
        log.trace("Enter");
        String tName = Thread.currentThread().getName();
        try {
            while (product == PRODUCT_NOT_READY) {
                System.out.println(tName + " waits");
                wait();
                System.out.println(tName + " wait is ended");
            }

            System.out.printf("%s consumes %d and then notifies all about it before terminating\n", tName, product);
            product = PRODUCT_NOT_READY;

            checkThreadStates();
            notifyAll();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        log.trace("Exit");
    }

    /**
     * Start producer and consumers, join the consumers, terminate and join the
     * producer.
     * 
     * @param args not used
     * @throws InterruptedException when a join is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        OneProdManyCons pc = new OneProdManyCons();

        // The producer is started first, and it is interrupted last
        Thread producer = new Thread(pc::produce, "P");
        producer.start();

        // Each consumer consumes a product and then terminates
        Thread[] consumers = { //
                new Thread(pc::consume, "C1"), //
                new Thread(pc::consume, "C2"), //
                new Thread(pc::consume, "C3") //
        };
        Arrays.stream(consumers).forEach(Thread::start);

        System.out.println("All threads started, main waits the consumers to join");
        for (Thread consumer : consumers) {
            consumer.join();
        }

        System.out.println("No more consumer, interrupting producer");
        producer.interrupt();
        producer.join();

        log.trace("Exit");
    }
}
