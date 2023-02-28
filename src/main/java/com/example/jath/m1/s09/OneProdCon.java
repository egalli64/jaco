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
 * One Producer - One Consumer
 */
public class OneProdCon {
    private static final int PRODUCT_NOT_READY = 0;

    private Random random = new Random();

    /** The resource shared between two threads */
    private int product = PRODUCT_NOT_READY;

    /**
     * The producer thread runs this method, that sets the shared resource.
     * 
     * Once the job is done it notifies the consumer thread about it.
     */
    private synchronized void producer() {
        product = random.nextInt(1, 7);
        System.out.printf("%s has produced %d%n", Thread.currentThread().getName(), product);
        // Since there is just one consumer, here notifyAll() would be an overkill
        notify();
    }

    /**
     * The consumer thread runs this method.
     * 
     * It waits the producer to set the product, then consumes it.
     */
    private synchronized void consumer() {
        String tName = Thread.currentThread().getName();
        try {
            // Wait until the product is available
            while (product == PRODUCT_NOT_READY) {
                System.out.println(tName + " waits for the result");
                wait();
                System.out.println(tName + " wait has ended");
            }

            // It is safe to assume that here product is ready to be consumed
            System.out.printf("%s consumes %d%n", tName, product);
            product = PRODUCT_NOT_READY;
        } catch (InterruptedException e) {
            // In this simple case, it is not legal interrupting a consumer
            throw new IllegalStateException(e);
        }
    }

    /**
     * Start consumer and producer, then join them.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        OneProdCon wn = new OneProdCon();

        System.out.println("Starting the consumer, it would wait for the producer");
        Thread consumer = new Thread(wn::consumer, "consumer");
        consumer.start();

        System.out.println("Starting the producer, it would notify the consumer");
        Thread producer = new Thread(wn::producer, "producer");
        producer.start();

        try {
            consumer.join();
            producer.join();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        System.out.println("Bye");
    }
}
