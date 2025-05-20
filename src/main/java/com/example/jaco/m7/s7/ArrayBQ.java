/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s7;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.example.jaco.m1.s3.FakeTask;

public class ArrayBQ {
    private static final Integer TERMINATOR = -1;

    public static void main(String[] args) {
        // notice the fixed capacity
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);

        Runnable producer = () -> {
            try {
                for (int i = 0; i < 12; i++) {
                    FakeTask.takeTime(10 * (i + 1));
                    queue.put(i);
                    System.out.printf("Delivered %d - queue: %d\n", i, queue.size());
                }

                queue.put(TERMINATOR);
                System.out.printf("Producer has terminated (queue: %d)\n", queue.size());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumer = () -> {
            try {
                Integer item = null;
                do {
                    item = queue.poll(250, TimeUnit.MILLISECONDS);
                    if (item != null) {
                        FakeTask.takeTime(100);
                        System.out.println("Consuming " + item + " - queue: " + queue.size());
                    } else {
                        System.out.println("Consumer timeout");
                    }
                } while (item != TERMINATOR);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        System.out.println("Starting ...");
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            executor.submit(producer);
            executor.submit(consumer);
        }
        System.out.println("... done!");
    }
}
