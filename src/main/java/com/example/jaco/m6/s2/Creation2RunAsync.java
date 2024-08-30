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
import com.example.jaco.m5.s7.CFutureBasicIntro;

/**
 * Simple creation of CompletableFuture by runAsync()
 */
public class Creation2RunAsync {
    private static final Logger log = LoggerFactory.getLogger(CFutureBasicIntro.class);

    public static void main(String[] args) {
        log.trace("Enter");

        Future<Void> cf = CompletableFuture.runAsync(() -> {
            log.debug("Enter");
            FakeTask.adder(100_000);
            log.debug("Exit");
        });

        while (!cf.isDone()) {
            FakeTask.adder(1_000);
        }

        log.debug("The future done now is {}", cf.isDone());
    }
}
