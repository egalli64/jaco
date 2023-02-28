/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m1.s08.reentrant;

/**
 * Reentrant lock
 */
public class Poodle extends Dog {
    /**
     * Let the poodle bark.
     * 
     * Being synchronized only one thread could run this code in a given moment.
     */
    @Override
    public synchronized void bark() {
        // if the synchronization was not reentrant the thread would hang here!
        super.bark();
        System.out.println("Bark variation as a poodle");
    }
}
