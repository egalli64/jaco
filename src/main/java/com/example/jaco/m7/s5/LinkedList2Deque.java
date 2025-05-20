/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s5;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Producer consumer on a ConcurrentLinkedDeque for high/low priority
 */
public class LinkedList2Deque {
    private static final Logger log = LoggerFactory.getLogger(LinkedList2Deque.class);

    private static final int PRODUCER_NR = 2;
    private static final int MESSAGE_NR = 10;

    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        Deque<Message> deque = new ConcurrentLinkedDeque<>();

        Runnable producerHigh = () -> {
            long id = Thread.currentThread().threadId();
            for (int i = 0; i < MESSAGE_NR; i++) {
                Message message = new Message(id, FakeTasks.calc(1_000));
                deque.offerFirst(message);
                log.info("High priority message added: {}", message);
            }
        };

        Runnable producerLow = () -> {
            long id = Thread.currentThread().threadId();
            for (int i = 0; i < MESSAGE_NR; i++) {
                Message message = new Message(id, FakeTasks.calc(1_000));
                deque.offerLast(message);
                log.info("Low priority message added: {}", message);
            }
        };

        Runnable consumer = () -> {
            for (int i = 0; i < MESSAGE_NR * PRODUCER_NR; i++) {
                Message message = deque.pollFirst();
                if (message != null) {
                    log.info("Consuming: {} -> {}", message, FakeTasks.calc(1_000) + message.payload);
                } else {
                    log.warn("No message in queue: {}", FakeTasks.calc(1_000));
                }
            }
        };

        try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
            executor.submit(producerHigh);
            executor.submit(producerLow);
            executor.submit(consumer);
        }

        log.trace("Exit");
    }

    private record Message(long id, double payload) {
    }
}
