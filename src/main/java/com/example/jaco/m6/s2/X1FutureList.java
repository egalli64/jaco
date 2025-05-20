/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s2;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Execute each FutureTask in a list, then get the results
 * <p>
 * Fixed Thread Pool sized POOL_SIZE for TASK_NR tasks
 */
public class X1FutureList {
    private static final Logger log = LoggerFactory.getLogger(X1FutureList.class);
    private static final int POOL_SIZE = 3;
    private static final int TASK_NR = 5;
    private static final int TASK_SIZE = 100;

    /**
     * <ul>
     * <li>create a list of FutureTasks
     * <li>execute each of them (as a Runnable) on a fixed thread pool
     * <li>extract the result from each future and give the user the grand total
     * </ul>
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        List<FutureTask<Double>> futures = Stream.generate(() -> new FutureTask<>(() -> FakeTasks.adder(TASK_SIZE)))
                .limit(TASK_NR).toList();

        try (ExecutorService es = Executors.newFixedThreadPool(POOL_SIZE)) {
            for (Runnable future : futures) {
                es.execute(future);
            }
        }

        ToDoubleFunction<Future<Double>> extract = future -> {
            try {
                return future.get();
            } catch (InterruptedException ex) {
                log.warn("No interruption was expected!", ex);
                Thread.currentThread().interrupt();
            } catch (ExecutionException ex) {
                log.error("Can't extract value from future", ex);
            }

            // Returning 0.0 as default value in case of error
            return 0.0;
        };

        System.out.println("Grand total: " + futures.stream().mapToDouble(extract).sum());

        log.trace("Exit");
    }
}
