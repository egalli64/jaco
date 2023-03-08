/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s04;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Stream;

/**
 * FutureTask on a Callable with exception
 * 
 * Dice caster, NR dice having values in [1..MAX]
 */
public class DiceCaster {
    private static final int NR = 3;
    private static final int MAX = 6;

    /**
     * A list of futures based on a callable is generated. Then the results are extracted.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        Callable<Integer> cast = () -> {
            Random random = new Random();
            // sometimes the die is lost!
            if (random.nextDouble() > 0.75) {
                throw new IllegalStateException("Lost die!");
            }
            return random.nextInt(1, MAX + 1);
        };

        var dice = Stream.generate(() -> new FutureTask<>(cast)).limit(NR).toList();

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