/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s08.reentrant;

/**
 * Reentrant lock
 */
public class Main {
    /**
     * A call to a synchronized method that calls another synchronized method on the same lock
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        new Poodle().bark();
    }
}
