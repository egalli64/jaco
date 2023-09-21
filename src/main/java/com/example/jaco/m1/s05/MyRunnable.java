/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s03.Jobs;

/**
 * A runnable, to be used for creating and running threads
 * <p>
 * It is not doing anything special, just taking some time in a fake job
 */
public class MyRunnable implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(MyRunnable.class);

    @Override
    public void run() {
        log.trace("Enter");

        Jobs.takeTime(100);

        log.trace("Exit");
    }
}
