/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s7;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Using an LinkedBlockingQueue (using the "unlimited" capacity - actually
 * Integer.MAX_VALUE) for a one to one producer/consumer
 */
public class ListBQ {
    private static final Logger log = LoggerFactory.getLogger(ListBQ.class);

    private static final Integer TERMINATOR = -1;
    private static final int NR_MESSAGES = 12;

    public static void main(String[] args) {
        log.trace("Enter");
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

        Runnable producer = () -> {
            log.trace("Enter");
            try {
                for (int i = 0; i < NR_MESSAGES; i++) {
                    // simulate (fast) resource production
                    FakeTasks.takeTime(10);
                    queue.put(i);
                    System.out.println("Produced " + i);
                    log.debug("Queue size is {}", queue.size());
                }

                queue.put(TERMINATOR);
                log.info("Last message produced - queue size is {}", queue.size());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Interrupt");
            }
            log.trace("Exit");
        };

        Runnable consumer = () -> {
            log.trace("Enter");
            try {
                Integer item = null;
                while ((item = queue.take()) != TERMINATOR) {
                    // simulate (slow) resource consumption
                    FakeTasks.takeTime(20);
                    System.out.println("Consumed " + item);
                    log.debug("Queue size is {}", queue.size());
                }
                log.info("Last message consumed - queue size is {}", queue.size());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Interrupt");
            }
            log.trace("Exit");
        };

        System.out.println("Starting ...");
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            executor.submit(producer);
            executor.submit(consumer);
        }

        log.trace("Exit");
    }
}
