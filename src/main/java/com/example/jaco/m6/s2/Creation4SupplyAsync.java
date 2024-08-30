/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m6.s2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Simple creation of CompletableFuture by supplyAsinc()
 */
public class Creation4SupplyAsync {
    private static final Logger log = LoggerFactory.getLogger(Creation4SupplyAsync.class);

    public static void main(String[] args) {
        log.trace("Enter");

        Future<Double> cf = CompletableFuture.supplyAsync(() -> {
            log.debug("Enter");
            return FakeTask.adder(100_000);
        });

        while (!cf.isDone()) {
            FakeTask.adder(1_000);
        }

        log.debug("The future done now is {}", cf.isDone());
    }
}
