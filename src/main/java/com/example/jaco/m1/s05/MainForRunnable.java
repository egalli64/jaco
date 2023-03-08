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
 * Working on a thread by composition
 * 
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
        String tName = "worker";
        System.out.printf("- The current state of thread '%s' -%n", tName);

        // The behavior we want to get from the worker
        Runnable my = new MyRunnable();

        // Inject the runnable in a thread object
        Thread worker = new Thread(my, tName);
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
