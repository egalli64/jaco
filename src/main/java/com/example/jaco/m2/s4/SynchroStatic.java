/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The synchronized static methods use their class as a lock
 */
public class SynchroStatic {
    private static final Logger log = LoggerFactory.getLogger(SynchroStatic.class);

    /**
     * Mixing calls to methods synchronized on this and on the class
     *
     * @param args not used
     * @throws InterruptedException when join is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        log.trace("Enter");
        SynchroStatic sy1 = new SynchroStatic();
        SynchroStatic sy2 = new SynchroStatic();

        Thread[] threads = { //
                new Thread(sy1::inst1, "ObjectOneA"), //
                new Thread(sy1::inst2, "ObjectOneB"), //
                new Thread(sy2::inst1, "ObjectTwoA"), //
                new Thread(sy2::inst2, "ObjectTwoB") //
        };

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
        log.trace("Exit");
    }

    /**
     * Synchronized on the class object
     */
    private synchronized static void stat1() {
        log.trace("Enter");
        System.out.println(Thread.currentThread().getName() + " static m1()");
        log.trace("Exit");
    }

    /**
     * Synchronized on the class object
     */
    private synchronized static void stat2() {
        log.trace("Enter");
        System.out.println(Thread.currentThread().getName() + " static m2()");
        log.trace("Exit");
    }

    /**
     * Synchronized on this
     */
    public synchronized void inst1() {
        log.trace("Enter");
        stat1();
        System.out.println(Thread.currentThread().getName() + " hello1()");
        stat2();
        log.trace("Exit");
    }

    /**
     * Synchronized on this
     */
    public synchronized void inst2() {
        log.trace("Enter");
        stat1();
        System.out.println(Thread.currentThread().getName() + " hello2()");
        stat2();
        log.trace("Exit");
    }
}
