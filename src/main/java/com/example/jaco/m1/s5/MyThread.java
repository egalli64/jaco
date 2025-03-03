/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * A thread class, to create threads for its run() method
 * <p>
 * It is not doing anything special, just taking some time in a fake job
 */
public class MyThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(MyThread.class);

    /**
     * Initialize the new thread with a given name
     * 
     * @param name the thread name
     */
    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        log.trace("Enter");

        FakeTask.takeTime(100);

        log.trace("Exit");
    }
}
