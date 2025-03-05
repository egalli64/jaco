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
 * Thread communication - synchronized block as monitor: wait/notifyAll
 * <p>
 * One Producer - Many Consumers, each consumer consumes a single product and
 * terminates
 */
public class OneProdManyCons {
    private static final Logger log = LoggerFactory.getLogger(OneProdManyCons.class);

    /** The resource shared between threads */
    private Product product = new Product();

    /**
     * Utility method to check thread states
     */
    private static synchronized void checkThreadStates() {
        log.trace("Enter");
        Thread[] ts = new Thread[6];
        // Thread.enumerate() should be used only for debugging and monitoring purposes
        int count = Thread.enumerate(ts);
        for (int i = 0; i < count; i++) {
            System.out.printf("%s is %s\n", ts[i].getName(), ts[i].getState());
        }
        log.trace("Exit");
    }

    /**
     * Generate products and notify consumers
     * <ul>
     * <li>Run until interrupted
     * <li>Notify all consumer after production
     * <li>Ensure a product is consumed before generating the next one
     */
    private synchronized void produce() {
        log.trace("Enter");

        int id = 0;

        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Producer is ready ... ");
                product.produce(id++, ThreadLocalRandom.current().nextInt(1, 7));
                System.out.printf("Producer generated %s and it is about to notify all for it\n", product);

                checkThreadStates();
                notifyAll();
                while (!product.isConsumed()) {
                    System.out.println("Producer waits product to be consumed");
                    wait();
                    System.out.println("Producer wait has ended");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Producer wait has been interrupted");
            Thread.currentThread().interrupt();
        } finally {
            if (!product.isConsumed()) {
                System.out.printf("Last product %s has not been consumed\n", product);
            }
            log.trace("Exit");
        }
    }

    /**
     * Each consumer thread runs this method
     * <p>
     * Wait for a product, consume it, then terminate
     * 
     * @throws IllegalStateException if the thread is interrupted
     */
    private synchronized void consume() {
        log.trace("Enter");
        String tName = Thread.currentThread().getName();
        try {
            while (!product.isProduced()) {
                System.out.println(tName + " waits for a product");
                wait();
                System.out.println(tName + " wait is ended");
            }

            int value = product.consume();
            System.out.printf("%s has consumed %d, and is about to notify all about it\n", tName, value);

            checkThreadStates();
            notifyAll();
        } catch (InterruptedException e) {
            // in this simple example, it is not legal interrupting a consumer
            log.warn("Consumer interrupted while waiting the product to be produced");
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

        System.out.println("All consumer finished, interrupting producer");
        producer.interrupt();
        producer.join();

        log.trace("Exit");
    }
}
