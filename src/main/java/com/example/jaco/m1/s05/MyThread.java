/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s03.Jobs;

/**
 * A thread class, to create threads for its run() method
 * 
 * It is not doing anything special, just some printing and sleeping
 */
public class MyThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(MyThread.class);

    /**
     * For demo, let the caller set the thread name on creation
     * 
     * @param name the assigned thread name
     */
    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        log.trace("Enter");

        Jobs.takeTime(100);

        log.trace("Exit");
    }
}
