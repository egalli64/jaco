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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Access to a synchronized collection by wrapping
 * <p>
 * Each thread should add() a given number of transactions to a list. The single
 * add() method is safe, thanks to synchronizedList()
 */
public class WrappedAdd {
    private static final Logger log = LoggerFactory.getLogger(WrappedAdd.class);

    private static final int THREAD_NR = 5;
    private static final int TRANSACTION_NR = 1_000;

    public static void main(String[] args) throws InterruptedException {
        List<String> list = Collections.synchronizedList(new ArrayList<>());

        Runnable task = () -> {
            for (int i = 0; i < TRANSACTION_NR; i++) {
                String id = Thread.currentThread().getName() + "_" + i;
                list.add(id);
            }
        };

        try (ExecutorService executor = Executors.newFixedThreadPool(THREAD_NR)) {
            for (int i = 0; i < THREAD_NR; i++) {
                executor.execute(task);
            }
        }

        System.out.printf("Expecting %d, result is %d\n", TRANSACTION_NR * THREAD_NR, list.size());
        log.trace("Exit");
    }
}