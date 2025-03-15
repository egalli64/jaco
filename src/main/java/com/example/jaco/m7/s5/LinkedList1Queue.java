/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s5;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Producer consumer on a ConcurrentLinkedQueue
 */
public class LinkedList1Queue {
    private static final Logger log = LoggerFactory.getLogger(LinkedList1Queue.class);

    private static final int PRODUCER_NR = 3;
    private static final int CONSUMER_NR = 1;
    private static final int THREAD_NR = PRODUCER_NR + CONSUMER_NR;
    private static final int MESSAGE_NR = 10;

    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        Queue<Message> queue = new ConcurrentLinkedQueue<>();

        Runnable producer = () -> {
            long id = Thread.currentThread().threadId();
            for (int i = 0; i < MESSAGE_NR; i++) {
                Message message = new Message(id, FakeTask.calc(1_000));
                queue.offer(message);
                log.info("Produced: {}", message);
            }
        };

        Runnable consumer = () -> {
            for (int i = 0; i < MESSAGE_NR * PRODUCER_NR; i++) {
                Message message = queue.poll();
                if (message != null) {
                    log.info("Consuming: {} -> {}", message, FakeTask.calc(1_000) + message.payload);
                } else {
                    log.warn("No message in queue: {}", FakeTask.calc(1_000));
                }
            }
        };

        try (ExecutorService executor = Executors.newFixedThreadPool(THREAD_NR)) {
            for (int i = 0; i < PRODUCER_NR; i++) {
                executor.submit(producer);
            }

            executor.submit(consumer);
        }

        log.trace("Exit");
    }

    private record Message(long id, double payload) {
    }
}