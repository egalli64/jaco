/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s7;

import java.lang.Thread.State;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Using Thread::isAlive() and Thread::join()
 * 
 * Enable assertions with -ea VM argument
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * Create, start, and join thread, checking if it is alive now and there.
     * 
     * @param args if at least an argument is passed, the main thread run a (fake)
     *             task to give time the worker to kick in
     */
    public static void main(String[] args) {
        log.trace("Enter");

        // Create another thread
        Thread worker = new Thread(() -> System.out.println("A message from the worker"), "worker");

        // The worker is NEW, not alive yet
        assert worker.getState() == State.NEW;
        if (!worker.isAlive()) {
            log.trace("Before start, {} is in state {}", worker.getName(), worker.getState());
        }

        worker.start();

        if (args.length > 0) {
            // in this case the worker should have time to terminate
            FakeTasks.takeTime(10);
        }

        // The worker can now be either RUNNABLE or TERMINATED
        State state = worker.getState();
        assert state == State.RUNNABLE || state == State.TERMINATED;
        log.trace("After start, and worker state is {}, meaning alive is {}", state, worker.isAlive());

        try {
            log.trace("Wait on worker termination");
            worker.join();
        } catch (InterruptedException ex) {
            // No one should interrupt the main thread join on the worker
            log.warn("This should not happen", ex);
            // we usually handle the interruption, at least resetting the interrupt flag
            Thread.currentThread().interrupt();
        }

        if (!worker.isAlive()) {
            log.trace("After joining in, the worker is not alive anymore");
        } else {
            log.warn("This should not happen. Maybe join has been interrupted?");
        }
        log.trace("Exit");
    }
}
