/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s5.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Working on a thread by derivation
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * Create and start a thread by instancing a class that extends Thread
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        System.out.println("- Create, start, and check the state of another thread");

        // Create another thread defined as extension of class Thread
        Thread worker = new MyThread("worker");
        // The thread state is NEW
        System.out.println("After creation, the worker thread state is " + worker.getState());

        worker.start();
        // The thread state should be RUNNABLE
        System.out.println("After starting, the worker thread state is " + worker.getState());

        while (worker.isAlive()) {
            // Doing something else in the main thread - this is NOT a common approach
            FakeTasks.takeTime(50);
        }

        // Now the worker is TERMINATED
        System.out.println("The worker thread state is now " + worker.getState());
        log.trace("Exit");
    }
}
