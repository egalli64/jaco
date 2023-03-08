/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s06;

import java.util.stream.DoubleStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s03.Jobs;

/**
 * What happens when an interrupt is sent to a waiting (here, sleeping) thread
 */
public class CheckInterruptedExceptional {
    private static final Logger log = LoggerFactory.getLogger(CheckInterruptedExceptional.class);

    /**
     * A thread is created and started. After a while an interrupt is sent to it. However, that thread
     * could be waiting for something to happen (here, the wait is simulated by sleeping). So the
     * interrupt could be managed in the "normal" way, or could require the catch of an
     * InterruptedException.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        // What is going to be executed by the worker. Only an interrupt will stop it.
        Runnable runnable = () -> {
            log.trace("Enter");
            final Thread cur = Thread.currentThread();

            try {
                while (!cur.isInterrupted()) {
                    System.out.print("(Fake) wait on a resource ... ");
                    // This is just a simulation! The use of sleep() in production code is very limited!
                    Thread.sleep(2);

                    double value = DoubleStream.generate(Math::random).limit(10).sum();
                    System.out.println(value + " calculated");
                }
                // thread interrupted when runnable
                log.info("(Fake) resource elaboration interrupted");
            } catch (InterruptedException e) {
                // thread interrupted when waiting, sleeping, or otherwise occupied
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
        Jobs.takeTime(20);

        System.out.println("Thread main decides it it time to cut it off");
        worker.interrupt();
        log.trace("Exit");
    }
}
