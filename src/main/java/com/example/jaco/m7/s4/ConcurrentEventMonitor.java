/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s4;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Register and query events in a ConcurrentSkipListMap
 */
public class ConcurrentEventMonitor {
    private static final Logger log = LoggerFactory.getLogger(ConcurrentEventMonitor.class);
    private static final int THREAD_NR = 5;
    private static final int EVENT_COUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");

        NavigableMap<Instant, Integer> events = new ConcurrentSkipListMap<>();

        // Task to register events in the event map
        Runnable registerer = () -> {
            for (int j = 0; j < EVENT_COUNT; j++) {
                Instant key = Instant.now().plusMillis(ThreadLocalRandom.current().nextInt(1_000));
                int value = ThreadLocalRandom.current().nextInt(100);
                events.put(key, value);
                log.trace("Event {}:{} registered", key, value);
            }
        };

        // Task to check for events in a range
        Runnable enquirer = () -> {
            Instant left = Instant.now().plusMillis(ThreadLocalRandom.current().nextInt(100));
            Instant right = Instant.now().plusMillis(ThreadLocalRandom.current().nextInt(200, 500));
            NavigableMap<Instant, Integer> sub = events.subMap(left, true, right, true);
            log.info("There are {} events from {} to {}", sub.size(), //
                    left.get(ChronoField.MILLI_OF_SECOND), right.get(ChronoField.MILLI_OF_SECOND));
            if (!sub.isEmpty()) {
                Instant first = sub.firstKey();
                int second = sub.higherKey(first).getNano();
                Instant last = sub.lastKey();
                int secondToLast = sub.lowerKey(last).getNano();
                log.info("[{}, {}, .. {}, {}]", first.getNano(), second, secondToLast, last.getNano());
            }
        };

        try (ExecutorService executor = Executors.newFixedThreadPool(THREAD_NR)) {
            for (int i = 0; i < THREAD_NR; i++) {
                executor.execute(registerer);
            }

            for (int i = 0; i < THREAD_NR; i++) {
                executor.execute(enquirer);
            }
        }

        log.trace("Exit");
    }
}
