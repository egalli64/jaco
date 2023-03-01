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
 * A simple exchange between a producer and a consumer
 */
public class OneProdCon {
    private double result;
    private boolean produced;
    private Lock lock;
    private Condition available;

    /**
     * Constructor
     */
    public OneProdCon() {
        this.result = 0.0;
        this.produced = false;
        this.lock = new ReentrantLock();
        this.available = lock.newCondition();
    }

    /**
     * For the producer thread.
     * 
     * Acquire the lock, produce the result, signal() on the condition its availability.
     */
    private void producer() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " enter producer()");

        try {
            lock.lock();
            result = Math.random();
            produced = true;
            System.out.printf("%s has produced as result %f%n", name, result);
            available.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * For the consumer thread.
     * 
     * Acquire the lock, if the product is not ready, await() on the condition for it. Then consume it.
     */
    private void consumer() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " enter consumer()");

        try {
            lock.lock();
            while (!produced) {
                System.out.println(name + " waits for the result");
                available.await();
                System.out.println(name + " wait is ended");
            }

            System.out.printf("%s consumes %f%n", name, result);
            produced = false;
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            lock.unlock();
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
        OneProdCon wn = new OneProdCon();

        Thread consumer = new Thread(wn::consumer, "TC");
        consumer.start();

        Thread producer = new Thread(wn::producer, "TP");
        producer.start();

        System.out.println("Main thread waits end of exachange");
        producer.join();
        consumer.join();

        System.out.println("Bye");
    }
}
