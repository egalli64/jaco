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

import com.example.jaco.FakeTasks;

/**
 * A semaphore that allows PERMIT_NR threads at a time to access a critical area
 */
public class SemaphoreTryAcquire {
    private static final Logger log = LoggerFactory.getLogger(SemaphoreTryAcquire.class);
    private static final int PERMIT_NR = 2;
    private static final int TASK_NR = 5;

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(PERMIT_NR);

        Runnable worker = () -> {
            if (semaphore.tryAcquire()) {
                // no more than PERMIT_NR tasks could be running in this critical area
                log.trace("Currently available permits: {}", semaphore.availablePermits());

                FakeTasks.takeTime(500);
                semaphore.release();
            } else {
                log.trace("No permit available");
            }
        };

        Stream.generate(() -> new Thread(worker)).limit(TASK_NR).forEach(Thread::start);
        log.trace("Exit, leave the children threads to do the work");
    }
}
