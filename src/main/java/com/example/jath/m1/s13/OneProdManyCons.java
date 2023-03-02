/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m1.s13;

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
    /** true when the product is not valid (anymore) */
    private boolean consumed;
    private Lock lock;
    private Condition availability;
    private Condition consumption;

    /**
     * Constructor
     */
    public OneProdManyCons() {
        this.product = 0.0;
        this.consumed = true;
        this.lock = new ReentrantLock();
        this.availability = lock.newCondition();
        this.consumption = lock.newCondition();
    }

    /**
     * For the producer thread, loop until interrupted.
     * 
     * Produce a product, signal it to all, then wait its consumption before produce again.
     */
    private void producer() {
        System.out.println("Producer in action");
        while (!Thread.currentThread().isInterrupted()) {
            lock.lock();
            try {
                while (!consumed) {
                    consumption.await();
                }
                product = Math.random();
                consumed = false;
                System.out.printf("Producer signals availability of %f%n", product);
                availability.signalAll();
            } catch (InterruptedException e) {
                System.out.println("Producer interrupted while waiting on consumption");
                return;
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * For the consumer thread.
     * 
     * Wait until it can consume to a not-already-consumed product. Signal that, and then terminate.
     */
    private void consumer() {
        String name = Thread.currentThread().getName();

        lock.lock();
        System.out.println(name + " in action");
        try {
            while (consumed) {
                availability.await();
            }

            System.out.printf("%s signals consumption of %f%n", name, product);
            consumed = true;
            consumption.signal();
        } catch (InterruptedException e) {
            System.out.println(name + " unexpectedly interrupted while waiting on availability");
            return;
        } finally {
            lock.unlock();
            System.out.println(name + " terminates");
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

        Thread[] consumers = { new Thread(wn::consumer, "C1"), new Thread(wn::consumer, "C2"),
                new Thread(wn::consumer, "C3") };
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
