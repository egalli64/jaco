/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s08;

/**
 * Avoid race condition when running legacy code not ready for multithreading execution.
 */
public class SynchroByRetrofit {
    /**
     * Four threads need to run a non-synchronized method. Provide synchronization as a wrapper.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("Synchronizing on the object before running a non-synchronized method");

        NoSynchro noSync = new NoSynchro();

        // Each thread runs a synchronized block containing just the call to the non-synchronized method
        Thread[] threads = { //
                new Thread(() -> {
                    synchronized (noSync) {
                        noSync.printStatus("Tom");
                    }
                }), new Thread(() -> {
                    synchronized (noSync) {
                        noSync.printStatus("Kim");
                    }
                }), new Thread(() -> {
                    synchronized (noSync) {
                        noSync.printStatus("Sal");
                    }
                }), new Thread(() -> {
                    synchronized (noSync) {
                        noSync.printStatus("Bob");
                    }
                }) //
        };

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
