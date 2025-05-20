/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * User of MyInterruptibleThread, show how to terminate a thread using a custom
 * approach
 */
public class CustomInterrupt {
    private static final Logger log = LoggerFactory.getLogger(CustomInterrupt.class);

    /**
     * Create and start a MyInterruptibleThread. Standard interrupt is ignored, use
     * shutdown() instead.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        MyInterruptibleThread worker = new MyInterruptibleThread("worker");

        System.out.println("!!! Race condition on System.out - expect a garbled output !!!");

        worker.start();

        try {
            // Let the worker kick in
            FakeTasks.takeTime(20);

            // Ask the worker to interrupt, but it does not cooperate
            System.out.println("Interrupting worker");
            worker.interrupt();
            garbler();
            if (worker.isAlive()) {
                System.out.println("Interrupt rejected");
            }

            // Ask the worker to shutdown, and this approach is accepted
            System.out.println("Shutting down worker ...");
            worker.shutdown();
            worker.join(50);
            if (!worker.isAlive()) {
                System.out.println("Shutdown accepted");
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        log.trace("Exit");
    }

    /**
     * Put some garbage on console, to show a race condition at work
     */
    private static void garbler() {
        for (int i = 0; i < 6; i++) {
            try {
                // Just a simulation! Thread.sleep() in seldom seen in production code
                Thread.sleep(7);
            } catch (InterruptedException e) {
                log.warn("Interrupted ignored!");
            }
            System.out.print((char) ('A' + i));
        }
    }
}
