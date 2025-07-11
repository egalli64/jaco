/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s3;

import java.lang.Thread.State;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread states
 * <p>
 * Enable assertions with -ea VM argument
 */
public class BaseCase {
    private static final Logger log = LoggerFactory.getLogger(BaseCase.class);

    /**
     * Create a thread with a simple task and let it run
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        String tName = "worker";
        System.out.printf("- Check the current state of thread '%s' -%n", tName);

        // Create a thread with a given name, the passed runnable is its task
        Thread t = new Thread(() -> System.out.printf("A message from %s: Hello!\n", tName), tName);

        // The thread is NEW, not already started
        assert t.getState() == State.NEW;
        System.out.printf("Thread %s is %s\n", t.getName(), t.getState());

        // Starting the thread, transition from NEW to RUNNABLE state
        t.start();

        // The worker could be running or maybe already terminated
        assert t.getState() == State.RUNNABLE || t.getState() == State.TERMINATED;
        System.out.printf("Thread %s is %s\n", t.getName(), t.getState());

        while (t.isAlive()) {
            log.info("After start, a thread is said to be alive");
        }
        log.info("We don't know when the worker thread is terminated");

        System.out.printf("Now thread %s is %s\n", t.getName(), t.getState());
        log.info("After termination, a thread is no more alive");

        System.out.println("- Main is about to terminate -");
    }
}
