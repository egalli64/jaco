/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s5;

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
            log.trace("The future job is not done, do something else");
            System.out.printf("Main thread result: %f%n", FakeTask.adder(10));
        }

        log.trace("Wait the future to complete");
        System.out.println("Worker result: " + cf.join());
        log.trace("Exit");
    }
}
