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
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * A call to a synchronized method that calls another synchronized method on the
     * same lock
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        Dog dog = new Poodle();
        System.out.println("Let the dog synchro-bark ...");
        dog.bark();

        log.trace("Exit");
    }
}
