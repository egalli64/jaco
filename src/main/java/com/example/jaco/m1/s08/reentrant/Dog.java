/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s08.reentrant;

/**
 * Reentrant lock
 */
public class Dog {
    /**
     * Let a dog bark.
     * 
     * Being synchronized only one thread could run this code in a given moment.
     */
    public synchronized void bark() {
        System.out.println("Barking as a dog");
    }
}