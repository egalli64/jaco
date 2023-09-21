/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s08.reentrant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reentrant lock
 */
public class Poodle extends Dog {
    private static final Logger log = LoggerFactory.getLogger(Poodle.class);

    /**
     * Let the poodle bark.
     * <p>
     * Being synchronized only one thread could run this code in a given moment.
     */
    @Override
    public synchronized void bark() {
        log.trace("Enter");
        // if the synchronization was not reentrant the thread would hang here!
        super.bark();
        System.out.println("Bark variation as a poodle");
        log.trace("Exit");
    }
}
