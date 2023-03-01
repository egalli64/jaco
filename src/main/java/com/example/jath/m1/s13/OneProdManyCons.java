/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m1.s13;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock and Condition
 * 
 * A simple exchange between a producer and a few consumers
 */
public class OneProdManyCons {
    private double product;
    private boolean consumed;
    private Lock lock;
    private Condition availability;

    /**
     * Constructor
     */
    public OneProdManyCons() {
        this.product = 0.0;
        this.consumed = true;
        this.lock = new ReentrantLock();
        this.availability = lock.newCondition();
    }

    /**
     * For the producer thread, loop until interrupted.
     * 
     * Produce a product, signal it to all, then wait its consumption before produce again.
     */
    private void producer() {
        try {
            lock.lock();
            System.out.println("Producer has acquired the lock");
            while (!Thread.currentThread().isInterrupted()) {
                product = Math.random();
                consumed = false;

                System.out.printf("Produced %f and signal availability%n", product);
                availability.signalAll();
                while (!consumed) {
                    System.out.println("Producer waits the product to be consumed");
                    availability.await(500, TimeUnit.MILLISECONDS);
                    System.out.println("Producer wait is ended");
                }
            }
            System.out.println("Producer production has been interrupted");
        } catch (InterruptedException e) {
            System.out.println("Producer wait has been interrupted");
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
            System.out.println("Producer has relased the lock and is terminating");
        }
    }

    /**
     * For the consumer thread.
     * 
     * Wait until has access to a not-already-consumed product. Signal that, and then terminate.
     */
    private void consumer() {
        String name = Thread.currentThread().getName();
        try {
            lock.lock();
            System.out.println(name + " has acquired the lock");
            while (consumed) {
                System.out.println(name + " waits for a product not already consumed");
                availability.await();
                System.out.println(name + " wait is ended");
            }

            System.out.printf("%s signals consumation of %f%n", name, product);
            consumed = true;
            availability.signalAll();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            lock.unlock();
            System.out.println(name + " has released the lock and is terminating");
        }
    }

    /**
     * Create and start the producer, then create the consumers.
     * 
     * Join on them, and then terminate the producer.
     * 
     * @param args not used
     * @throws InterruptedException when interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        OneProdManyCons wn = new OneProdManyCons();

        Thread producer = new Thread(wn::producer);
        producer.start();

        Thread[] consumers = { new Thread(wn::consumer, "TC1"), new Thread(wn::consumer, "TC2"),
                new Thread(wn::consumer, "TC3") };
        for (Thread consumer : consumers) {
            consumer.start();
        }

        for (Thread consumer : consumers) {
            consumer.join();
        }

        System.out.println("No more consumer, interrupting producer");
        producer.interrupt();
        producer.join();

        System.out.println("Bye");
    }
}
