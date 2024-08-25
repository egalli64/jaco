/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m4.s2;

import java.util.concurrent.Semaphore;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * A semaphore that allows two threads at a time to access a critical area
 * <p>
 */
public class SemaphoreExample {
    private static final Logger log = LoggerFactory.getLogger(SemaphoreExample.class);
    private static final int PERMIT_NR = 2;
    private static final int TASK_NR = 5;

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(PERMIT_NR);

        Runnable worker = () -> {
            try {
                semaphore.acquire();
                // no more than PERMIT_NR tasks could be running in this critical area
                FakeTask.takeTime(500);
                semaphore.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Unexpected", e);
            }
        };

        Stream.generate(() -> new Thread(worker)).limit(TASK_NR).forEach(Thread::start);
        log.trace("Exit, leave the children threads to do the work");
    }
}
