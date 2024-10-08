/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s3;

import java.lang.Thread.State;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

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

    /**
     * Simulate to be busy doing something for a while
     * 
     * @param millis required duration
     */
    public static void takeTimeAndCheckMain(long millis) {
        takeTime(millis);

        Thread[] ts = new Thread[2];
        // Thread.enumerate() should be used for debugging and monitoring purposes only
        final int count = Thread.enumerate(ts);
        for (int i = 0; i < count; i++) {
            String name = ts[i].getName();
            if (name.equals("main")) {
                State state = ts[i].getState();

                // the main thread state should be waiting - being in join on this thread
                assert state == State.WAITING;
                System.out.printf("Thread %s is %s\n", name, state);
                break;
            }
        }
    }

    /**
     * Sum up a few random doubles
     * 
     * @param size number of items used to generate the result
     * @return a double
     */
    public static double adder(int size) {
        double result = DoubleStream.generate(ThreadLocalRandom.current()::nextDouble).limit(size).sum();
        log.trace("Returning {}", result);
        return result;
    }

    /**
     * Taking some time performing a useless calculation
     * 
     * @param size number of items used to generate the result
     * @return a double
     */
    public static double calc(int size) {
        return DoubleStream.generate(ThreadLocalRandom.current()::nextDouble).limit(size).map(Math::cbrt).sum();
    }
}
