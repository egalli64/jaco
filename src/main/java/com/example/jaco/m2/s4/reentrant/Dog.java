/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s4.reentrant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reentrant lock
 */
public class Dog {
    private static final Logger log = LoggerFactory.getLogger(Dog.class);

    /**
     * Let a dog bark.
     * <p>
     * Being synchronized only one thread could run this code in a given moment.
     */
    public synchronized void bark() {
        log.trace("Enter");
        System.out.println("Barking as a dog");
        log.trace("Exit");
    }
}
