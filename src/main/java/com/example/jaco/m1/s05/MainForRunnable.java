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
 * Working on a thread by composition
 * <p>
 * The injected runnable is an instance of a class implementing Runnable
 */
public class MainForRunnable {
    private static final Logger log = LoggerFactory.getLogger(MainForRunnable.class);

    /**
     * Create and start a thread by instancing a Thread injected with a Runnable
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        System.out.println("- Create, start, and check the state of another thread");

        // Inject a runnable in a thread
        Thread worker = new Thread(new MyRunnable());
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
