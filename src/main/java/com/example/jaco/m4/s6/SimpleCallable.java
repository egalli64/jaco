/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m4.s6;

import java.util.concurrent.Callable;
import java.util.stream.DoubleStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A callable to be used by an Executor
 */
public class SimpleCallable implements Callable<Double> {
    private static final Logger log = LoggerFactory.getLogger(SimpleCallable.class);

    @Override
    public Double call() throws Exception {
        log.trace("Enter");

        double result = DoubleStream.generate(() -> Math.cbrt(Math.random())).limit(100).sum();

        log.trace("Exit {}", result);
        return result;
    }
}
