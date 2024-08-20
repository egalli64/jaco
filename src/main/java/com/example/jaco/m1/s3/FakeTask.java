/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fake task
 */
public class FakeTask {
    private static final Logger log = LoggerFactory.getLogger(FakeTask.class);

    /**
     * Simulate to be busy doing something for a while
     * 
     * @param millis required duration
     */
    public static void takeTime(long millis) {
        try {
            log.trace("Do something for (about) {} ms", millis);
            // In production code you won't see often calls to Thread.sleep()
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
