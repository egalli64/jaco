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
        System.out.println("- Main starts a worker thread -");

        // Create and start a thread that runs on a specified method
        Thread t1 = new Thread(Waiting::aMethod, "worker");
        t1.start();

        // Do something else on main thread, so we can safely assume t1 kicks in
        FakeTask.takeTime(50);

        // Now t1 is expected to be timed waiting
        assert t1.getState() == State.TIMED_WAITING;
        System.out.printf("Thread %s is %s\n", t1.getName(), t1.getState());

        log.info("Wait {} to terminate", t1.getName());
        // when join() enters, the main thread is going to WAIT
        t1.join();

        System.out.printf("From main, checking %s state: %s\n", t1.getName(), t1.getState());
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

        FakeTask.takeTime(500);

        // getAllStackTraces() should be used for debugging, monitoring, profiling only!
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("main")) {
                State state = t.getState();
                assert state == State.WAITING;
                System.out.printf("Thread main is %s\n", state);
                break;
            }
        }

        log.trace("Exit");
    }
}
