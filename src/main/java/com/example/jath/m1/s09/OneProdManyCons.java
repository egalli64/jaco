/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m1.s09;

import java.util.Random;

/**
 * Thread communication
 * 
 * One Producer - Many Consumers
 */
public class OneProdManyCons {
    private static final int PRODUCT_NOT_READY = 0;

    private Random random = new Random();

    /** The resource shared between threads */
    private int product = PRODUCT_NOT_READY;

    /**
     * Utility method for demonstration purposes
     */
    private static synchronized void checkThreadStates() {
        System.out.println("- Checking thread states from " + Thread.currentThread().getName());
        Thread[] ts = new Thread[6];
        // Thread::enumerate() should be used only for debugging and monitoring purposes
        int count = Thread.enumerate(ts);
        for (int i = 0; i < count; i++) {
            System.out.printf("%s is %s%n", ts[i].getName(), ts[i].getState());
        }
        System.out.println("---");
    }

    /**
     * The producer thread runs this method, that sets the shared resource.
     * <li>The production has to be terminated by interrupt.
     * <li>After the production the producer notify all to consume it.
     * <li>If there is a non-consumed product, the producer wait for its consumption
     */
    private synchronized void producer() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Producer is ready ... ");
                product = random.nextInt(1, 7);
                System.out.println("Producer has generated " + product);

                checkThreadStates();
                notifyAll();
                while (product != PRODUCT_NOT_READY) {
                    System.out.println("Producer waits");
                    wait(500);
                    System.out.println("Producer wait has ended");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Producer wait has been interrupted");
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Producer is about to terminate");
        }
    }

    /**
     * The consumer thread runs this method.
     * 
     * It waits the producer to set the product, then consumes it.
     */
    private synchronized void consumer() {
        String tName = Thread.currentThread().getName();
        try {
            while (product == PRODUCT_NOT_READY) {
                System.out.println(tName + " waits");
                wait();
                System.out.println(tName + " wait is ended");
            }

            System.out.printf("Consumer %s consumes %d and then notifies all about it%n", tName, product);
            product = PRODUCT_NOT_READY;

            checkThreadStates();
            notifyAll();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        System.out.println(tName + " is about to terminate");
    }

    /**
     * Start producer and consumers, then join first the consumers then the producer.
     * 
     * @param args not used
     */
    public static void main(String[] args) throws InterruptedException {
        OneProdManyCons wn = new OneProdManyCons();

        Thread producer = new Thread(wn::producer, "TP");

        Thread[] consumers = { //
                new Thread(wn::consumer, "TC1"), //
                new Thread(wn::consumer, "TC2"), //
                new Thread(wn::consumer, "TC3") //
        };

        producer.start();
        for (Thread consumer : consumers) {
            consumer.start();
        }

        System.out.println("All threads started, main waits the consumers to join");
        for (Thread consumer : consumers) {
            consumer.join();
        }

        System.out.println("No more consumer, interrupting producer");
        producer.interrupt();
        producer.join();

        System.out.println("Bye");
    }
}
