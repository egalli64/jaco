/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m7.s3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jaco.FakeTasks;

/**
 * Avoid ConcurrentModificationException
 */
public class WrapperModificationFix {
    private static final Logger log = LoggerFactory.getLogger(WrapperModificationFix.class);

    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        List<Double> list = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 10; i++) {
            double value = FakeTasks.calc(100);
            list.add(value);
        }

        Thread tReader = new Thread(() -> {
            // critical section on the loop
            synchronized (list) {
                for (Double value : list) {
                    log.info("The reader result is {}", value);
                }
            }
        });

        Thread tDropper = new Thread(() -> {
            log.info("The dropper result is {}", FakeTasks.calc(30_000));

            list.remove(0);
            log.info("Now list has size {}", list.size());
        });

        tReader.start();
        tDropper.start();

        tReader.join();
        tDropper.join();
        log.trace("Exit");
    }
}
