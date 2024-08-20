/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.Jobs;

/**
 * User of MyInterruptibleThread, show how to terminate a thread using a custom approach
 */
public class CustomInterrupt {
    private static final Logger log = LoggerFactory.getLogger(CustomInterrupt.class);

    /**
     * Create and start a MyInterruptibleThread. Standard interrupt is ignored, use shutdown() instead.
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
            Jobs.takeTime(20);

            // Ask the worker to interrupt, but it does not cooperate
            System.out.println("Interrupting worker");
            worker.interrupt();
            worker.join(50);
            if (worker.isAlive()) {
                System.out.println("Interrupt rejected");
            }

            // Ask the worker to shutdown, and this approach is accepted
            System.out.println("Shutting down worker");
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
}
