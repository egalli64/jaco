/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s11;

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

        int i = ai.get();
        System.out.printf("An AtomicInteger (%s) is a wrapper around a volatile int (%d)%n", ai, i);

        i = 1;
        ai.set(i);
        System.out.printf("Call set(%d) and then get() it: %s%n", i, ai.get());

        i = 2;
        int j = ai.getAndSet(i);
        System.out.printf("Call getAndSet(%d) gives %d [previous value], get() gives %s%n", i, j, ai.get());
        System.out.printf("Call incrementAndGet() gives %d [new value]%n", ai.incrementAndGet());
        System.out.printf("Call addAndGet(%d) gives %d [new value]%n", i, ai.addAndGet(i), ai.get());
    }
}