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
 * Show waiting and time waiting threads
 * <p>
 * Enable assertion with -ea VM argument
 */
public class Waiting {
    private static final Logger log = LoggerFactory.getLogger(Waiting.class);

    /**
     * Create a thread that goes immediately in timed waiting state.
     * <p>
     * Put the main thread in wait for the end of the child thread, then terminate.
     * 
     * @param args not used
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("- A thread that would wait and be waited -");

        // Create and start a thread that runs on a specified method
        Thread t1 = new Thread(Waiting::aMethod, "worker");
        t1.start();

        // Do something else on main thread, so we can safely assume t1 kicks in
        Jobs.takeTime(50);

        // Now t1 is expected to be timed waiting
        assert t1.getState() == State.TIMED_WAITING;
        System.out.printf("Checking %s from main: it is %s%n", t1.getName(), t1.getState());

        log.info("Wait {} to terminate", t1.getName());
        // when join() enters, the main thread is going to WAIT
        t1.join();

        System.out.println("- Main is about to terminate -");
    }

    /**
     * A method meant to run in its own thread.
     * <p>
     * It simulates a timed waiting on a resource, then check that main is waiting
     * for its termination.
     */
    public static void aMethod() {
        log.info("Simulating a timed wait on a resource");
        try {
            // This is just a simulation! The use of sleep() in production code is very
            // limited!
            Thread.sleep(500);
            log.trace("Resource acquired");

            System.out.printf("Checking main from %s: ", Thread.currentThread().getName());
            Thread[] ts = new Thread[2];
            // Thread::enumerate() should be used for debugging and monitoring purposes only
            int count = Thread.enumerate(ts);
            for (int i = 0; i < count; i++) {
                if (ts[i].getName().equals("main")) {
                    State state = ts[i].getState();

                    // the main thread state should be waiting - being in join on this thread
                    assert state == State.WAITING;
                    System.out.println("it is " + state);
                    break;
                }
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        log.trace("Exit");
    }
}
