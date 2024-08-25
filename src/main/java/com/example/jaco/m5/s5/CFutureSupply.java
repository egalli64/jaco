/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m5.s5;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * A CompletableFuture created by supplyAsync()
 */
public class CFutureSupply {
    private static final Logger log = LoggerFactory.getLogger(CFutureSupply.class);

    public static void main(String[] args) {
        log.trace("Enter");
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(() -> FakeTask.adder(10));

        if (!cf.isDone()) {
            log.trace("The future is not done, do something else");
            System.out.println("Main thread result: " + FakeTask.adder(10));
        } else {
            log.info("It could happen that the future is done, but it would be quite rare");
        }

        log.trace("Wait the future to complete");
        System.out.println("Worker result: " + cf.join());
        log.trace("Exit");
    }
}
