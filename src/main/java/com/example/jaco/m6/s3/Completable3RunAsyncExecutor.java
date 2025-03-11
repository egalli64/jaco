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
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Simple creation of CompletableFuture by runAsync() with provided executor
 */
public class Completable3RunAsyncExecutor {
    private static final Logger log = LoggerFactory.getLogger(Completable3RunAsyncExecutor.class);
    private static final int NR_THREADS = 2;

    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");

        // the task we want to run asynchronously as CompletableFuture
        Runnable task = () -> {
            log.debug("Enter");
            FakeTask.adder(40_000);
        };

        try (ExecutorService es = Executors.newFixedThreadPool(NR_THREADS)) {
            // a list of CompletableFuture, each of them running on the current executor
            List<CompletableFuture<Void>> futures = IntStream.range(0, 4)
                    .mapToObj(i -> CompletableFuture.runAsync(task, es)).toList();

            // do some work in the main thread
            for (Future<Void> future : futures) {
                while (!future.isDone()) {
                    log.debug("While at least a future is not done ...");
                    FakeTask.adder(4_000);
                }
            }
        }

        log.trace("Exit");
    }
}