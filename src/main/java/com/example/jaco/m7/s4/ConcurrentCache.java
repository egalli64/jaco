/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s4;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.m1.s3.FakeTask;

/**
 * Cache Expensive objects in a ConcurrentHashMap
 */
public class ConcurrentCache {
    private static final Logger log = LoggerFactory.getLogger(ConcurrentCache.class);
    private static final int THREAD_NR = 8;
    private static final int OBJECT_COUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");

        Map<Integer, Expensive> cache = new ConcurrentHashMap<>();

        /**
         * Each thread create the expensive object, only one put it in the cache
         */
        Runnable putter = () -> {
            // given an expensive object
            Expensive expensive = new Expensive(42, Thread.currentThread().getName());

            // if it is not already in the cache, put it in
            cache.putIfAbsent(42, expensive);
        };

        /**
         * One thread create, the other ones fetch it from cache
         */
        Runnable creator = () -> {
            String tName = Thread.currentThread().getName();
            for (int id = 0; id < OBJECT_COUNT; id++) {
                // only if the expensive object is not in the cache, create and put it in
                cache.computeIfAbsent(id, key -> new Expensive(key, tName));

                // then use the created/fetched object for a random time
                FakeTask.calc(10 * ThreadLocalRandom.current().nextInt(100));
            }
        };

        /**
         * Each thread create the expensive object, then remove it if found in the cache
         */
        Runnable remover = () -> {
            // given an expensive object
            Expensive expensive = new Expensive(42, Thread.currentThread().getName());

            // if it is not already in the cache, remove it
            if (cache.remove(42, expensive)) {
                log.info("Remove {} from cache", expensive);
            }
        };

        /**
         * Each thread create two expensive object, if the first one is in the cache,
         * replace it with the second one
         */
        Runnable replacer = () -> {
            // given an expensive object
            Expensive expensive = new Expensive(1, Thread.currentThread().getName());
            Expensive extra = new Expensive(1, "extra");

            // if it is not already in the cache, remove it
            if (cache.replace(1, expensive, extra)) {
                log.info("Replace {} in cache", extra);
            }
        };

        try (ExecutorService executor = Executors.newFixedThreadPool(THREAD_NR)) {
            for (int i = 0; i < THREAD_NR; i++) {
                executor.execute(putter);
            }

            for (int i = 0; i < THREAD_NR; i++) {
                executor.execute(creator);
            }

            for (int i = 0; i < THREAD_NR; i++) {
                executor.execute(remover);
            }

            for (int i = 0; i < THREAD_NR; i++) {
                executor.execute(replacer);
            }
        }

        log.trace("Exit");
    }

    public record Expensive(int id, String creator) {
        public Expensive {
            log.trace("Create {}:{}", id, creator);
        }
    }
}