/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s4;

import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ThreadLocal example
 * <ul>
 * <li>Create a ThreadLocalCacheWorker
 * <li>Create a list of threads on the worker
 * <li>Run them
 * </ul>
 * Notice how each thread has its own cache
 */
public class ThreadLocalMain {
    private static final Logger log = LoggerFactory.getLogger(ThreadLocalMain.class);
    private static final int TASK_NR = 6;

    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");

        ThreadLocalCacheWorker worker = new ThreadLocalCacheWorker();

        List<Thread> threads = Stream.generate(() -> new Thread(worker)).limit(TASK_NR).toList();
        threads.stream().forEach(Thread::start);
        for (Thread t : threads) {
            t.join();
        }

        log.trace("Exit");
    }
}
