/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s4;

import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A FutureTask that returns a String
 */
public class ASimpleFutureTask {
    private static final Logger log = LoggerFactory.getLogger(ASimpleFutureTask.class);

    /**
     * Create a future task, run it in another thread, until is not done do
     * something else, then print its result and terminate.
     * 
     * @param args not used
     * @throws Exception if anything goes wrong in the future task
     */
    public static void main(String[] args) throws Exception {
        log.trace("Enter");

        FutureTask<String> myTask = new FutureTask<>(() -> {
            log.trace("Future task started");
            return "Future task result is " + aJob(5_000);
        });
        new Thread(myTask).start();

        log.trace("While the future task works, do something else in the main thread");
        while (!myTask.isDone()) {
            System.out.println("A main thread result is " + aJob(5));
        }

        System.out.println(myTask.get());
        log.trace("Exit");
    }

    /**
     * A simple job that takes some time
     * 
     * @param size size of the job
     * @return a double
     */
    private static double aJob(int size) {
        log.trace("Enter");
        return DoubleStream.generate(() -> Math.cbrt(ThreadLocalRandom.current().nextDouble())).limit(size).sum();
    }
}
