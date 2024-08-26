/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m4.s5;

import java.util.concurrent.Exchanger;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exchanger on a String
 */
public class MessageExchanger {
    private static final Logger log = LoggerFactory.getLogger(MessageExchanger.class);
    private static final int TASK_NR = 6;

    public static void main(String[] args) {
        log.trace("Enter");
        Exchanger<String> exchanger = new Exchanger<>();

        Runnable task = () -> {
            try {
                String message = "Message from " + Thread.currentThread().getName();
                log.info("Produced: " + message);

                message = exchanger.exchange(message);
                log.info("After exchange: " + message);
            } catch (InterruptedException e) {
                log.warn("Unexpected", e);
                Thread.currentThread().interrupt();
            }
        };

        Stream.generate(() -> new Thread(task)).limit(TASK_NR).forEach(Thread::start);

        // note: the program hangs till the last child thread terminates
    }
}
