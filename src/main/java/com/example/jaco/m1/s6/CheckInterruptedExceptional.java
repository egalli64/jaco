/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s6;

import java.util.stream.DoubleStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * What happens when an interrupt is sent to a waiting (here, sleeping) thread
 */
public class CheckInterruptedExceptional {
    private static final Logger log = LoggerFactory.getLogger(CheckInterruptedExceptional.class);

    /**
     * A thread is created and started. After a while an interrupt is sent to it.
     * However, that thread could be waiting for something to happen (here, the wait
     * is simulated by sleeping). So the interrupt could be managed in the "normal"
     * way, or could require the catch of an InterruptedException.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        // Behavior of the worker thread, it runs until interrupted
        Runnable runnable = () -> {
            log.trace("Enter");
            final Thread cur = Thread.currentThread();

            try {
                while (!cur.isInterrupted()) {
                    System.out.print("(Fake) wait on a resource ... ");
                    // Just a simulation! Thread.sleep() in seldom seen in production code
                    // Put the current thread in TIMED_WAITING state
                    Thread.sleep(2);

                    double value = DoubleStream.generate(Math::random).limit(10).sum();
                    System.out.println("The result is " + value);
                }
                // thread interrupted when in RUNNING state
                log.info("(Fake) resource elaboration interrupted");
            } catch (InterruptedException e) {
                // thread interrupted when in blocked or waiting state
                log.info("(Fake) wait on resource acquisition interrupted", e);
                // reset the flag on the current thread as interrupted
                cur.interrupt();
            }
            log.trace("Exit");
        };

        Thread worker = new Thread(runnable, "worker");

        System.out.println("!!! Race condition on System.out - expect a garbled output !!!");

        worker.start();

        // Let the worker kick in
        FakeTask.takeTime(20);

        // the worker could be running or waiting
        System.out.println("Worker state now is " + worker.getState());

        System.out.println("Thread main decides it it time to cut it off");
        worker.interrupt();

        // Let the worker time to manage the interrupt
        FakeTask.takeTime(5);
        System.out.printf("Worker is interrupted (%b) and its state is %s\n", //
                worker.isInterrupted(), worker.getState());

        log.trace("Exit");
    }
}
