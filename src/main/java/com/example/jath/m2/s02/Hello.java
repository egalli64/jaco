/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s02;

import java.util.stream.DoubleStream;

/**
 * A runnable for Executor examples
 */
public class Hello implements Runnable {
    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.printf("%s begin @ %d%n", name, System.currentTimeMillis() % 1_000);
        System.out.printf("%s {%f}%n", name, DoubleStream.generate(Math::random).limit(100_000).map(Math::cbrt).sum());
        System.out.printf("%s end @ %d%n", name, System.currentTimeMillis() % 1_000);
    }

}
