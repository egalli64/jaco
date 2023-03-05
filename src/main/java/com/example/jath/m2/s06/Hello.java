/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s06;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.DoubleStream;

/**
 * A Callable to be used by an Executor
 */
public class Hello implements Callable<Integer> {
    private static AtomicInteger idGenerator = new AtomicInteger();

    private int id;

    /**
     * Constructor
     */
    public Hello() {
        this.id = idGenerator.getAndIncrement();
    }

    @Override
    public Integer call() throws Exception {
        String name = Thread.currentThread().getName();

        System.out.printf("%s: Hello %d%n", name, id);
        System.out.printf("%s: %f%n", name, aJob(100));
        System.out.printf("%s: Goodbye %d%n", name, id);

        return id;
    }

    /**
     * A simple job that takes some time
     * 
     * @param size size of the job
     * @return a double
     */
    private static double aJob(int size) {
        return DoubleStream.generate(() -> Math.cbrt(Math.random())).limit(size).sum();
    }
}
