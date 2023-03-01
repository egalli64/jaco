/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m1.s10;

/**
 * Thread communication
 * 
 * Weak synchronization by volatile. More thread could work on a volatile variable, but only one of
 * them could change it.
 */
public class Volatile {
    private static volatile boolean run = true;
    private static volatile int counter = 0;

    /**
     * Main thread and runner communicates over two volatile variables. No need of synchronization.
     * 
     * @param args not used
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            System.out.println("Starting worker");
            // run is read
            while (run) {
                // counter is written
                counter += 1;
            }
            System.out.println("Terminating worker");
        });
        worker.start();

        // do something in main thread, give time to worker thread to kick in
        double x = Math.cbrt(Math.PI);
        System.out.println("Calculated by main thread: " + x);

        // run is written
        run = false;
        worker.join();

        // counter is read
        System.out.println("Calculated by worker: " + counter);
    }
}
