/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s3;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Simple creation of CompletableFuture by supplyAsync() with provided executor
 */
public class Completable5SupplyAsyncExecutor {
    private static final Logger log = LoggerFactory.getLogger(Completable5SupplyAsyncExecutor.class);
    private static final int NR_THREADS = 2;

    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");

        // the task we want to run asynchronously as CompletableFuture
        Supplier<Double> task = () -> {
            log.debug("Enter");
            return FakeTasks.adder(40_000);
        };

        List<CompletableFuture<Double>> futures;
        try (ExecutorService es = Executors.newFixedThreadPool(NR_THREADS)) {
            // a list of CompletableFuture, each running on the current executor
            futures = IntStream.range(0, 4).mapToObj(i -> CompletableFuture.supplyAsync(task, es)).toList();

            // do some work in the main thread
            for (Future<Double> future : futures) {
                while (!future.isDone()) {
                    log.debug("While at least a future is not done ...");
                    FakeTasks.adder(4_000);
                }
            }
        }

        Function<Future<Double>, Double> mapper = f -> {
            try {
                return f.get();
            } catch (Exception e) {
                throw new RuntimeException("Failed to get result from future", e);
            }
        };

        double result = futures.stream().map(mapper).mapToDouble(Double::doubleValue).sum();
        System.out.println("Total result from futures: " + result);

        log.trace("Exit");
    }
}
