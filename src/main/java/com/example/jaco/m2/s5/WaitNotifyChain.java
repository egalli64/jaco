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
public class WaitNotifyChain {
    private static final Logger log = LoggerFactory.getLogger(WaitNotifyChain.class);

    private static final int COUNT = 6;

    private boolean ready = false;
    private int data;

    private synchronized void produce() {
        log.trace("Enter");

        for (int i = 1; i <= COUNT; i++) {
            while (ready) {
                try {
                    log.trace("Wait");
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            // set data and the control flag
            data = i;
            ready = true;

            log.trace("Notify the production of data {}", data);
            notify();
        }
        log.trace("Exit");
    }

    /**
     * synchronized provides memory visibility guarantees
     */
    private synchronized void consume() {
        log.trace("Enter");

        for (int i = 1; i <= COUNT; i++) {
            while (!ready) {
                try {
                    log.trace("Wait");
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            System.out.println("Message received: " + data);
            ready = false;

            log.trace("Notify the consumption of data");
            notify();
        }

        log.trace("Exit");
    }

    public static void main(String[] args) {
        log.trace("Enter");

        WaitNotifyChain resource = new WaitNotifyChain();

        Thread consumer = new Thread(() -> {
            resource.consume();
        }, "Consumer");

        Thread producer = new Thread(() -> {
            resource.produce();
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
