/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s4;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Approaching to atomic variables
 */
public class AtomicHello {
    /**
     * A few basic methods on AtomicInteger
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        AtomicInteger ai = new AtomicInteger(42);

        final int i42 = ai.get();
        System.out.printf("An AtomicInteger (%s) is a wrapper around a volatile int (%d)%n", ai, i42);

        final int i1 = 1;
        ai.set(i1);
        System.out.printf("Call set(%d) and then get() it: %s%n", i1, ai.get());

        final int i2 = 2;
        final int j1 = ai.getAndSet(i2);
        System.out.printf("Call getAndSet(%d) gives %d [previous value], get() gives %s%n", i2, j1, ai.get());
        System.out.printf("Call incrementAndGet() gives %d [new value]%n", ai.incrementAndGet());
        System.out.printf("Call addAndGet(%d) gives %d [new value]%n", i2, ai.addAndGet(i2));
    }
}
