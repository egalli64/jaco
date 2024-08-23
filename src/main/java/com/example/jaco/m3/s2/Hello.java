/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s2;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A runnable for Executor examples
 */
public class Hello implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Hello.class);

    @Override
    public void run() {
        log.trace("Enter");
        System.out.printf("On %s generated value is %f\n", Thread.currentThread().getName(),
                DoubleStream.generate(ThreadLocalRandom.current()::nextDouble).limit(100).map(Math::cbrt).sum());
        log.trace("Exit");
    }
}
