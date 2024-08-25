/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.IntToDoubleFunction;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executor, ThreadPoolExecutor via Executors::newCachedThreadPool()
 */
public class CachedThreadPoolExecutorExplicit {
    private static final Logger log = LoggerFactory.getLogger(CachedThreadPoolExecutorExplicit.class);
    private static final int TASK_NR = 3;

    /**
     * The Cached Thread Pool Executor starts with a core pool sized zero.
     * <p>
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
        IntToDoubleFunction filler = n -> DoubleStream.generate(ThreadLocalRandom.current()::nextDouble).limit(n).map(Math::cbrt).sum();

        System.out.println("Running the first batch of jobs ...");
        batch.accept(TASK_NR);

        System.out.println("Doing something else in main: " + filler.applyAsDouble(1_000_000));

        System.out.println("Running the second batch of jobs ...");
        batch.accept(TASK_NR);

        System.out.println("Doing something else in main: " + filler.applyAsDouble(50_000));

        System.out.println("Running the third batch of jobs (double size) ...");
        batch.accept(TASK_NR * 2);

        // explicit shutdown - before Java 19
        executor.shutdown();
        log.trace("Exit");
    }
}
