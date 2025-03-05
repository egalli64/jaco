/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s4.reentrant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A specialized dog that overrides the barking method
 * <p>
 * Reentrant locking: both Poodle.bark() and Dog.bark() are synchronized
 * implicitly on the same object (this). A thread holding the lock can call the
 * superclass method without deadlocking itself
 */
public class Poodle extends Dog {
    private static final Logger log = LoggerFactory.getLogger(Poodle.class);

    /**
     * Overrides the bark method, adding a poodle variation
     * <p>
     * It is a synchronized method. The call to super.bark() is safe
     */
    @Override
    public synchronized void bark() {
        log.trace("Enter");
        // if the synchronization was not reentrant the thread would deadlock here!
        super.bark();
        System.out.println("Bark variation as a poodle");
        log.trace("Exit");
    }
}
