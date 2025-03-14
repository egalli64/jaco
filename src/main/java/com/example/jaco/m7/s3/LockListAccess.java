/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Access to a collection protecting the critical section with a Lock
 */
public class LockListAccess {
    private static final Logger log = LoggerFactory.getLogger(LockListAccess.class);

    private static final int LIST_SIZE = 10;
    private static final int THREAD_NR = 5;
    private static final int RUN_NR = 100_000;

    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");

        List<Integer> list = new ArrayList<>(Collections.nCopies(LIST_SIZE, 0));
        Lock lock = new ReentrantLock();

        Runnable task = () -> {
            for (int i = 0; i < RUN_NR; i++) {
                // notice that in this example the list size is immutable
                for (int j = 0; j < list.size(); j++) {
                    // critical section
                    lock.lock();
                    try {
                        list.set(j, list.get(j) + 1);
                    } finally {
                        lock.unlock();
                    }
                }
            }
        };

        try (ExecutorService executor = Executors.newFixedThreadPool(THREAD_NR)) {
            for (int i = 0; i < THREAD_NR; i++) {
                executor.execute(task);
            }
        }

        int result = list.stream().mapToInt(Integer::intValue).sum();
        System.out.printf("Expecting %d, result is %d\n", LIST_SIZE * THREAD_NR * RUN_NR, result);
        log.trace("Exit");
    }
}