/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s6;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExecutorService::awaitTermination()
 * <p>
 * Fixed Thread Pool sized POOL_SIZE for TASK_NR tasks
 */
public class WaitingTermination {
    private static final Logger log = LoggerFactory.getLogger(WaitingTermination.class);
    private static final int POOL_SIZE = 2;
    private static final int TASK_NR = 5;

    public static void main(String[] args) throws Exception {
        log.trace("Enter");
        ExecutorService es = Executors.newFixedThreadPool(POOL_SIZE);

        List<Future<Double>> futures = Stream.generate(SimpleCallable::new) //
                .map(h -> es.submit(h)).limit(TASK_NR).toList();
        shutdownAndAwaitTermination(es);

        // Future::get() throws InterruptedException, ExecutionException
        for (Future<Double> future : futures) {
            System.out.println("Result " + future.get() + " delivered");
        }
        log.trace("Exit");
    }

    /**
     * Shutdown plus awaitTermination, from Java documentation with minor changes
     * <p>
     * Functionality implemented in {@link ExecutorService#close()} (version 19)
     * 
     * @param pool the executor currently running
     * @see <a href=
     *      "https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html">The
     *      ExecutorService Java documentation<a>
     */
    static void shutdownAndAwaitTermination(ExecutorService pool) {
        log.trace("Enter");
        // Disable new tasks from being submitted
        pool.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                // Cancel currently executing tasks
                pool.shutdownNow();
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ex) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        log.trace("Exit");
    }
}