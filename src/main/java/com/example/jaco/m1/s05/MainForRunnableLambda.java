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
 * Injecting a runnable defined locally
 */
public class MainForRunnableLambda {
    private static final Logger log = LoggerFactory.getLogger(MainForRunnableLambda.class);

    /**
     * Create and start a thread by instancing a Thread injected with a Runnable
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");
        String tName = "worker";
        System.out.printf("- The current state of thread '%s' -%n", tName);

        // The behavior we want to get from the other thread
        Runnable runnable = () -> {
            log.trace("Enter");
            Jobs.takeTime(100);
            log.trace("Exit");
        };

        // Inject the runnable in a thread object
        Thread worker = new Thread(runnable, tName);
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
