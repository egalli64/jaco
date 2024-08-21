/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The JVM creates and starts the main thread to run the main method
 * <p>
 * Two Thread static methods: currentThread() and sleep()
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.printf("The thread running the main method is named %s\n", Thread.currentThread().getName());

        final long delta = 1_000;
        System.out.printf("About to sleep @ %d for %d millis\n", System.currentTimeMillis(), delta);
        try {
            // Thread.sleep() is seldom seen in production code!
            Thread.sleep(delta);
        } catch (InterruptedException e) {
            log.error("Sleep interrupted", e);
        }

        System.out.printf("...sleep ended @ %d%n", System.currentTimeMillis());
    }
}
