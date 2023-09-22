/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s04;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FutureTask on a Callable with exception
 * <p>
 * Dice caster, NR dice having values in [1..MAX]
 */
public class DiceCaster {
    private static final Logger log = LoggerFactory.getLogger(DiceCaster.class);

    private static final int NR = 3;
    private static final int MAX = 6;

    /**
     * A list of futures based on a callable is generated. Then the results are
     * extracted.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        // The callable for the future tasks
        Callable<Integer> cast = () -> {
            log.trace("Enter");
            Random random = new Random();
            // sometimes a die get lost
            if (random.nextDouble() > 0.75) {
                log.trace("Lost die");
                throw new IllegalStateException("A lost die!");
            }
            int result = random.nextInt(1, MAX + 1);
            log.trace("This cast generated a {} as result", result);
            return result;
        };

        // A list of future tasks, each of them based on the Callable "cast"
        List<FutureTask<Integer>> dice = Stream.generate(() -> new FutureTask<>(cast)).limit(NR).toList();

        // Put each future task in a thread, and start it
        dice.stream().forEach(die -> new Thread(die).start());

        // Hangs on each die until it completes its job, than print it
        for (var die : dice) {
            try {
                System.out.println(die.get());
            } catch (InterruptedException ex) {
                log.warn("No interruption was expected!", ex);
                throw new IllegalStateException(ex);
            } catch (ExecutionException ex) {
                // An exception thrown by a FutureTask is wrapped in an ExecutionException
                System.out.println("No result: " + ex.getCause().getMessage());
            }
        }

        log.trace("Exit");
    }
}