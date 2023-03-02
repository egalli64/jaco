/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s02;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Executor, ThreadPoolExecutor via Executors::newFixedThreadPool()
 */
public class FixedThreadExecutor {
    private static final int POOL_SIZE = 2;

    /**
     * The Fixed Thread Pool Executor puts the extra task in a queue and run them as soon as a thread
     * gets available.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("Fixed " + POOL_SIZE + " Thread Pool on Runnables");
        Executor executor = Executors.newFixedThreadPool(POOL_SIZE);
        for (int i = 0; i < 5; i++) {
            executor.execute(new Hello());
        }
    }
}