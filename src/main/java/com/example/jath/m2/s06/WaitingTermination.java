/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m2.s06;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * ExecutorService::awaitTermination()
 */
public class WaitingTermination {
    private static final int POOL_SIZE = 2;
    private static final int TASK_NR = 5;

    public static void main(String[] args) throws Exception {
        System.out.printf("Fixed Thread Pool sized %d for %d tasks%n", POOL_SIZE, TASK_NR);
        ExecutorService es = Executors.newFixedThreadPool(POOL_SIZE);

        List<Future<Integer>> futures = Stream.generate(() -> es.submit(new Hello())).limit(TASK_NR).toList();
        shutdownAndAwaitTermination(es);

        for (Future<Integer> future : futures) {
            System.out.println("Result " + future.get() + " delivered");
        }
    }

    /**
     * Use of shutdown plus awaitTermination, as seen on Java documentation, with minor changes
     * 
     * @param pool the executor currently running
     * 
     * @see <a href=
     *      "https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html">The
     *      ExecutorService Java documentation<a>
     */
    static void shutdownAndAwaitTermination(ExecutorService pool) {
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
    }
}