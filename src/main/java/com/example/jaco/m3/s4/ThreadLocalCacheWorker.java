/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m3.s4;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A runnable encapsulating a ThreadLocal cache
 */
public class ThreadLocalCacheWorker implements Runnable {
    private static final int MAX_SIZE = 5;
    private static final int BOUND = 100;
    private static ThreadLocal<Set<Integer>> cache = ThreadLocal.withInitial(TreeSet::new);

    private static final Logger log = LoggerFactory.getLogger(ThreadLocalCacheWorker.class);

    @Override
    public void run() {
        log.trace("Enter");
        Set<Integer> localCache = cache.get();

        for (int i = 0; i < MAX_SIZE; i++) {
            localCache.add(ThreadLocalRandom.current().nextInt(BOUND));
        }

        log.info("Cache: {}", localCache);
    }
}
