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
 * Chain of exchanges between a producer and a consumer. Two conditions are used to let producer and
 * consumer communicate about their state.
 */
public class OneProdConChain {
    /** How many product the producer is going to produce */
    private static final int PRODUCT_NR = 3;

    private double product;
    /** false if the product is not ready for consumption */
    private boolean produced;
    private Lock lock;
    private Condition availablility;
    private Condition consumation;

    /**
     * Constructor
     */
    public OneProdConChain() {
        this.product = 0.0;
        this.produced = false;
        this.lock = new ReentrantLock();
        this.availablility = lock.newCondition();
        this.consumation = lock.newCondition();
    }

    /**
     * For the producer thread.
     * 
     * Produce the requested products, once a time, signaling its availability then terminate.
     */
    private void producer() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " in action");

        for (int i = 0; i < PRODUCT_NR; i++) {
            lock.lock();
            try {
                while (produced) {
                    System.out.println(name + " waits the product being consumed");
                    consumation.await();
                    System.out.println(name + " is signaled of product consumption");
                }
                product = Math.random();
                produced = true;
                System.out.printf("%s signals production of %f%n", name, product);
                availablility.signal();
            } catch (InterruptedException e) {
                System.out.println(name + " unexpected interruption when waiting on consumation");
                return;
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * For the consumer thread.
     * 
     * Acquire the lock, if the product is not ready, await() on the condition for it. Then consume it.
     */
    private void consumer() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " in action");

        while (!Thread.currentThread().isInterrupted()) {
            lock.lock();
            try {
                while (!produced) {
                    System.out.println(name + " waits for a product");
                    availablility.await();
                    System.out.println(name + " is signaled of product availablility");
                }

                System.out.printf("%s signals that %f has been consumed%n", name, product);
                produced = false;
                consumation.signal();
            } catch (InterruptedException e) {
                System.out.println(name + " wait on availability interrupted");
                return;
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Create and start a consumer thread, that it is going to hang, waiting for the producer. Then
     * create and start a producer thread, that would signal its production to the consumer.
     * 
     * @param args not used
     * @throws InterruptedException when interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        OneProdConChain wn = new OneProdConChain();

        Thread consumer = new Thread(wn::consumer, "C");
        consumer.start();

        Thread producer = new Thread(wn::producer, "P");
        producer.start();

        System.out.println("Main waits producer to terminate");
        producer.join();

        System.out.println("Main interrupts the consumer");
        consumer.interrupt();
        consumer.join();

        System.out.println("Bye");
    }
}
