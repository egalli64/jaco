package com.example.jath.m2.s03;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CompletableFuture2T {
    public static void main(String[] args) {
        Future<Double> adder = futureCubeRootAdder(10);

        if (adder.isDone()) {
            System.out.println("Unexpected, the adder should take some time to complete!");
        } else {
            System.out.println("While the adder works, do something else in the main thread");
        }

        System.out.println("When there is nothing more to do, wait the adder to complete");
        try {
            System.out.println(adder.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Future<Double> futureCubeRootAdder(int count) {
        System.out.println("Create and start the completable future task");
        CompletableFuture<Double> future = new CompletableFuture<>();
        new Thread(() -> {
            double result = 0.0;
            for (int i = 0; i < count; i++) {
                result += Math.cbrt(Math.random());
            }
            future.complete(result);
        }).start();

        return future;
    }
}
