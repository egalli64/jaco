package com.example.jath.m2.s02;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ASimpleFutureTask {
    public static void main(String[] args) {
        FutureTask<String> myTask = new FutureTask<>(() -> {
            System.out.println("The future task has started");
            return "Future task result is " + aJob(300);
        });

        System.out.println("Starting the future task from main");
        new Thread(myTask).start();

        if (myTask.isDone()) {
            System.out.println("Unexpected, the future task should take some time to complete!");
        } else {
            System.out.println("While the future task works, do something else in the main thread");
        }

        while (!myTask.isDone()) {
            System.out.println("A main thread result is " + aJob(100));
        }

        try {
            System.out.println(myTask.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static double aJob(int times) {
        double result = 0.0;
        for (int i = 0; i < times; i++) {
            result += Math.cbrt(Math.random());
        }
        return result;
    }
}
