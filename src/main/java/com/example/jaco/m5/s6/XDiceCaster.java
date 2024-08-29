/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s6;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An Executor for a list of FutureTask
 * <p>
 * Dice caster, NR dice having values in [1..MAX]
 */
public class XDiceCaster {
    private static final Logger log = LoggerFactory.getLogger(XDiceCaster.class);

    private static final int DICE_NR = 5;
    private static final int MAX_VALUE = 6;
    private static final int TASK_NR = 2;
    private static final double DIE_LOSS_PROBABILITY = 0.25;

    /**
     * <ul>
     * <li>A callable defines how a die is thrown
     * <li>A list of FutureTask based on the callable is generated
     * <li>All the tasks are submitted to an executor
     * <li>Let the executor shutdown, all the tasks have time to complete
     * <li>Extract the results from the futures
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        // 1. a callable that generates a die cast result (or exception, for lost die)
        Callable<Integer> cast = () -> {
            // sometimes a die get lost
            if (ThreadLocalRandom.current().nextDouble() < DIE_LOSS_PROBABILITY) {
                log.trace("A lost die");
                throw new IllegalStateException();
            }
            int result = ThreadLocalRandom.current().nextInt(1, MAX_VALUE + 1);
            log.trace("Cast {}", result);
            return result;
        };

        // 2. a list of DICE_NR future tasks, each based on the cast callable
        List<FutureTask<Integer>> dice = IntStream.range(0, DICE_NR).mapToObj(i -> new FutureTask<>(cast)).toList();

        // 3. run a fixed thread pool executor, each task is submitted
        try (ExecutorService executor = Executors.newFixedThreadPool(TASK_NR)) {
            dice.forEach(executor::submit);
        }
        // 4. at this point, each task has been completed

        // 5. extract the result from each future
        for (Future<Integer> die : dice) {
            try {
                System.out.println(die.get());
            } catch (InterruptedException ex) {
                log.warn("No interruption was expected!", ex);
                Thread.currentThread().interrupt();
            } catch (ExecutionException ex) {
                // An exception thrown by a FutureTask is wrapped in an ExecutionException
                System.out.println("No result");
            }
        }

        log.trace("Exit");
    }
}
