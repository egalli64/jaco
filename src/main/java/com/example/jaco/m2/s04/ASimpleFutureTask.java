/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s04;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.DoubleStream;

/**
 * A FutureTask that returns a String
 */
public class ASimpleFutureTask {
    /**
     * Create a future task, run it in another thread, until is not done do something else, then print
     * its result and terminate.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        FutureTask<String> myTask = new FutureTask<>(() -> {
            System.out.println("The future task has started");
            return "Future task result is " + aJob(300);
        });

        System.out.println("Starting the future task from main");
        new Thread(myTask).start();

        System.out.println("While the future task works, do something else in the main thread");
        while (!myTask.isDone()) {
            System.out.println("A main thread result is " + aJob(100));
        }

        try {
            System.out.println(myTask.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
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
