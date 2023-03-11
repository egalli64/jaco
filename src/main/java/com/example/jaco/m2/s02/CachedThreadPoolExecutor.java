/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s02;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.IntToDoubleFunction;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executor, ThreadPoolExecutor via Executors::newCachedThreadPool()
 */
public class CachedThreadPoolExecutor {
    private static final Logger log = LoggerFactory.getLogger(CachedThreadPoolExecutor.class);
    private static final int TASK_NR = 3;

    /**
     * The Cached Thread Pool Executor starts with a core pool sized zero.
     * 
     * More threads are created only if required.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        ExecutorService executor = Executors.newCachedThreadPool();

        // Pass n Hello runnable to the executor
        Consumer<Integer> batch = n -> Stream.generate(Hello::new).limit(n).forEach(executor::execute);
        // Time waster
        IntToDoubleFunction filler = (n) -> DoubleStream.generate(Math::random).limit(n).map(Math::cbrt).sum();

        // first batch
        batch.accept(TASK_NR);

        System.out.println("Doing something else in main: " + filler.applyAsDouble(1_000_000));
        System.out.println("Then assigning other tasks to the executor");

        // second batch
        batch.accept(TASK_NR);

        System.out.println("Doing something else in main: " + filler.applyAsDouble(50_000));
        System.out.println("Then assigning other tasks to the executor");

        // third batch, requires more threads
        batch.accept(TASK_NR * 2);

        executor.shutdown();
        log.trace("Exit");
    }
}
