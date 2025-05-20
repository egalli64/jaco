/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Shutdown plus awaitTermination, from Java documentation with minor changes
 */
public class Shutdown3Await {
    private static final Logger log = LoggerFactory.getLogger(Shutdown3Await.class);
    private static final int TASK_NR = 5;

    /**
     * Ask to a single thread executor to run a few tasks, but then shutdown
     * <p>
     * Wait for pending tasks to be terminated before going on
     * 
     * @param args not used
     * @see <a href=
     *      "https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ExecutorService.html">The
     *      ExecutorService Java documentation<a>
     */
    public static void main(String[] args) {
        log.trace("Enter");

        ExecutorService es = Executors.newSingleThreadExecutor();
        for (int i = 0; i < TASK_NR; i++) {
            es.execute(() -> FakeTasks.adder(1_000_000));
        }

        es.shutdown();
        System.out.println("Calling for shutdown");

        try {
            // Wait a while for existing tasks to terminate
            if (!es.awaitTermination(10, TimeUnit.SECONDS)) {
                // Cancel currently executing tasks
                es.shutdownNow();
                // Wait a while for tasks to respond to being cancelled
                if (!es.awaitTermination(10, TimeUnit.SECONDS))
                    log.error("Pool did not terminate");
            }
        } catch (InterruptedException ex) {
            // (Re-)Cancel if current thread also interrupted
            log.error("Shutdown interrupted, forcing shutdown", ex);
            es.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }

        log.trace("Exit");
    }
}
