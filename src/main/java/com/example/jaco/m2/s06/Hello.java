/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s06;

import java.util.concurrent.Callable;
import java.util.stream.DoubleStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Callable to be used by an Executor
 */
public class Hello implements Callable<Double> {
    private static final Logger log = LoggerFactory.getLogger(Hello.class);

    @Override
    public Double call() throws Exception {
        log.trace("Enter");

        double result = DoubleStream.generate(() -> Math.cbrt(Math.random())).limit(100).sum();

        log.trace("Exit {}", result);
        return result;
    }
}
