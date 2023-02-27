/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m1.s06;

/**
 * Working with two threads, one is going to interrupt the other one.
 * 
 * Notice that the console is carelessly shared between two threads. Expect troubles.
 */
public class CheckInterrupted {
    /**
     * Create another thread and start it. After a while interrupt it.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("In thread " + Thread.currentThread().getName());

        // What is going to be executed by the other thread. Only an interrupt will stop it.
        Runnable runnable = () -> {
            final Thread cur = Thread.currentThread();
            System.out.println("In thread " + cur.getName());

            int i = 0;
            while (!cur.isInterrupted()) {
                System.out.print("Simulating a job in ");
                System.out.print(cur.getName());
                System.out.print(" ... ");
                System.out.println(i++);
            }

            System.out.printf("Thread %s is done%n", cur.getName());
        };

        Thread other = new Thread(runnable, "other");

        System.out.println("!!! Two threads are going to compete on System.out !!!");
        System.out.println("!!! Output could be garbled  !!!");

        other.start();

        for (int i = 0; i < 100; i++) {
            System.out.printf("Simulating a job in %s ... ", Thread.currentThread().getName());
            System.out.println(i);
        }

        System.out.println("Thread main has enough of other thread!");
        other.interrupt();

        System.out.printf("Thread %s is done%n", Thread.currentThread().getName());
    }
}
