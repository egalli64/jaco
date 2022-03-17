package com.example.jath.m2.s02;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class DiceCaster {
    private static final int NR = 3;
    private static final int MAX_VALUE = 6;

    public static void main(String[] args) {
        @SuppressWarnings("unchecked")
        FutureTask<Integer>[] dice = new FutureTask[NR];
        for (int i = 0; i < dice.length; i++) {
            dice[i] = new FutureTask<>(() -> {
                if (Math.random() > 0.5) {
                    throw new IllegalStateException("The die is lost!");
                }
                return (int) Math.ceil(Math.random() * MAX_VALUE);
            });
        }

        for (var die : dice) {
            new Thread(die).start();
        }

        for (var die : dice) {
            try {
                System.out.println(die.get());
            } catch (InterruptedException e) {
                System.err.println("No interruption is expected here");
                throw new IllegalStateException(e);
            } catch (ExecutionException e) {
                System.out.println(e.getCause().getMessage());
            }
        }

        System.out.println("done");
    }
}