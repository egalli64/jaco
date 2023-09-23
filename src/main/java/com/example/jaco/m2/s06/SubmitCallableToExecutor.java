/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s06;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExecutorService::submit() for Callable
 * <p>
 * Fixed Thread Pool sized POOL_SIZE for TASK_NR tasks
 */
public class SubmitCallableToExecutor {
    private static final Logger log = LoggerFactory.getLogger(SubmitCallableToExecutor.class);
    private static final int POOL_SIZE = 2;
    private static final int TASK_NR = 5;

    /**
     * Create a list of Futures, as generated by submit to an executor, then
     * shutdown, then get the result from each future.
     * <p>
     * All the tasks are processed by the executor.
     * 
     * @param args not used
     * @throws Exception from Future::get()
     */
    public static void main(String[] args) throws Exception {
        log.trace("Enter");
        ExecutorService es = Executors.newFixedThreadPool(POOL_SIZE);

        List<Future<Double>> futures = Stream.generate(SimpleCallable::new) //
                .map(h -> es.submit(h)).limit(TASK_NR).toList();
        es.shutdown();

        // Future::get() throws InterruptedException, ExecutionException
        for (Future<Double> future : futures) {
            System.out.printf("Result %f delivered%n", future.get());
        }
        log.trace("Exit");
    }
}