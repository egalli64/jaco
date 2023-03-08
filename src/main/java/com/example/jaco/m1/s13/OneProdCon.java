/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s13;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock and Condition
 * 
 * A single exchange between a producer and a consumer
 */
public class OneProdCon {
    private double product;
    private boolean produced;
    private Lock lock;
    private Condition availability;

    /**
     * Constructor
     */
    public OneProdCon() {
        this.product = 0.0;
        this.produced = false;
        this.lock = new ReentrantLock();
        this.availability = lock.newCondition();
    }

    /**
     * For the producer thread.
     * 
     * Produce a single product, signal its availability, then terminate.
     */
    private void producer() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " enter in action");

        lock.lock();
        try {
            product = Math.random();
            produced = true;
            System.out.printf("%s signal availability of %f%n", name, product);
            availability.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * For the consumer thread.
     * 
     * Wait for a product, consume it, then terminate.
     */
    private void consumer() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " enter in action");

        lock.lock();
        try {
            while (!produced) {
                System.out.println(name + " waits");
                availability.await();
                System.out.println(name + " is signaled of product availability");
            }

            System.out.printf("%s consumes %f%n", name, product);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Create and start producer and consumer. Let them play, then terminate.
     *
     * Notice that the timing is not deterministic.
     * 
     * @param args not used
     * @throws InterruptedException when interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        OneProdCon wn = new OneProdCon();

        Thread[] threads = { new Thread(wn::consumer, "C"), new Thread(wn::producer, "P") };

        for (Thread t : threads) {
            t.start();
        }

        System.out.println("Main waits");
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Bye");
    }
}
