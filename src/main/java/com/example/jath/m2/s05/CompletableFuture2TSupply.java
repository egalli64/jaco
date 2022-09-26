package com.example.jath.m2.s05;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CompletableFuture2TSupply {
    public static void main(String[] args) {
        System.out.println("Create and start the completable future task");
        Future<Double> adder = CompletableFuture.supplyAsync(() -> {
            double result = 0.0;
            for (int i = 0; i < 10; i++) {
                result += Math.cbrt(Math.random());
            }
            return result;
        });

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
}
