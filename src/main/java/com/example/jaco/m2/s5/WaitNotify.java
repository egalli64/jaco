/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple thread communication - synchronized block as monitor
 * <p>
 * The consumer waits for the producer, the producer notifies the consumer
 */
public class WaitNotify {
    private static final Logger log = LoggerFactory.getLogger(WaitNotify.class);

    private boolean ready = false;
    private String data;

    private synchronized void produce(String data) {
        log.trace("Enter");

        // synchronized ensures changes are seen across threads
        this.data = data;
        this.ready = true;

        // business logic completed, notify
        notify();

        // after notify, we could see non-business related code (debugging, monitoring)
        log.trace("Exit");
    }

    /**
     * synchronized provides memory visibility guarantees
     */
    private synchronized void consume() {
        log.trace("Enter");

        // synchronized ensures changes are seen across threads
        while (!ready) {
            try {
                wait();
                System.out.println("Message received: " + data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        ready = false;
        log.trace("Exit");
    }

    public static void main(String[] args) {
        log.trace("Enter");

        WaitNotify resource = new WaitNotify();

        Thread consumer = new Thread(() -> {
            resource.consume();
        }, "Consumer");

        Thread producer = new Thread(() -> {
            resource.produce("Hello!");
        }, "Producer");

        consumer.start();
        producer.start();

        try {
            consumer.join();
            producer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.trace("Exit");
    }
}
