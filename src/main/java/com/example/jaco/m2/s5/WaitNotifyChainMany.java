/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s5;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Simple thread communication - synchronized block as monitor
 * <p>
 * The consumer waits for the producer, the producers notifies the consumer
 */
public class WaitNotifyChainMany {
    private static final Logger log = LoggerFactory.getLogger(WaitNotifyChainMany.class);

    private static final int COUNT = 10;

    private boolean ready = false;
    private int data;

    private synchronized void produce(int count) {
        log.trace("Enter");

        for (int i = 1; i <= count; i++) {
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

            log.trace("Notify the production of data {} to the consumers", data);
            notifyAll();
        }

        log.trace("Exit");
    }

    private synchronized void consume() {
        log.trace("Enter");

        try {
            while (!ready) {
                log.trace("Wait");
                wait();
            }

            System.out.printf("Message %d received\n", data);
            ready = false;

            log.trace("Notify the consumption of data");
            notifyAll();
        } catch (InterruptedException e) {
            log.info("Interrupt!");
            Thread.currentThread().interrupt();
        }

        log.trace("Exit");
    }

    /**
     * Wrapper around consume
     */
    private void consumeMany() {
        int count = 0;

        while (!Thread.currentThread().isInterrupted()) {
            count += 1;
            consume();

            // simulate use of data
            FakeTasks.calc(1_000 * count);
        }

        log.info("Terminated while waiting product {}", count);
    }

    public static void main(String[] args) {
        log.trace("Enter");

        WaitNotifyChainMany resource = new WaitNotifyChainMany();

        Thread[] consumers = { //
                new Thread(resource::consumeMany, "Consumer1"), //
                new Thread(resource::consumeMany, "Consumer2"), //
                new Thread(resource::consumeMany, "Consumer3") //
        };

        Arrays.stream(consumers).forEach(Thread::start);

        Thread producer = new Thread(() -> resource.produce(COUNT), "Producer");
        producer.start();
        try {
            producer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Producer has terminated, shutdown the consumers");
        Arrays.stream(consumers).forEach(Thread::interrupt);

        for (Thread consumer : consumers) {
            try {
                consumer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        log.trace("Exit");
    }
}
