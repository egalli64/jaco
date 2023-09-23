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
 * Since Java 19 ExecutorService::awaitTermination() is called implicitly by
 * closing the executor service
 * <p>
 * Fixed Thread Pool sized POOL_SIZE for TASK_NR tasks
 */
public class WaitingTermination19 {
    private static final Logger log = LoggerFactory.getLogger(WaitingTermination19.class);
    private static final int POOL_SIZE = 2;
    private static final int TASK_NR = 5;

    public static void main(String[] args) throws Exception {
        log.trace("Enter");
        ExecutorService es = Executors.newFixedThreadPool(POOL_SIZE);

        List<Future<Double>> futures = Stream.generate(SimpleCallable::new) //
                .map(h -> es.submit(h)).limit(TASK_NR).toList();
        es.close();

        // Future::get() throws InterruptedException, ExecutionException
        for (Future<Double> future : futures) {
            System.out.println("Result " + future.get() + " delivered");
        }
        log.trace("Exit");
    }
}