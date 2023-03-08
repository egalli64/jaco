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
 * Working on a thread by derivation
 */
public class MainForMyThread {
    private static final Logger log = LoggerFactory.getLogger(MainForMyThread.class);

    /**
     * Create and start a thread by instancing a class that extends Thread
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");
        String tName = "worker";

        // Create another thread defined as extension of class Thread
        System.out.printf("- The current state of thread '%s' -%n", tName);
        Thread worker = new MyThread(tName);
        System.out.printf("%s is %s%n", worker.getName(), worker.getState());

        worker.start();
        System.out.printf("%s is %s%n", worker.getName(), worker.getState());

        // Doing something else in the main thread
        Jobs.takeTime(300);

        // Now the worker _should_ be TERMINATED
        System.out.printf("%s is %s%n", worker.getName(), worker.getState());
        log.trace("Exit");
    }
}
