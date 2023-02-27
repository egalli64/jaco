/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jath
 */
package com.example.jath.m1.s03;

import java.lang.Thread.State;

/**
 * Thread states
 * 
 * Enable assertions with -ea VM argument
 */
public class BaseCase {
    /**
     * Create a simple thread and let it run
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        String name = "base";
        System.out.printf("Thread states for %s%n", name);

        // Create a thread with a given name, the passed runnable is its task
        Thread t = new Thread(() -> System.out.printf("Hello from %s%n", name), name);

        // The thread is in its initial state
        assert t.getState() == State.NEW && !t.isAlive();
        System.out.printf("Thread %s state is %s%n", t.getName(), t.getState());

        // Starting the thread, transition from NEW to RUNNABLE state
        t.start();

        // The process now has two alive threads, main and base
        assert t.getState() == State.RUNNABLE || t.getState() == State.TERMINATED;
        System.out.printf("Thread %s state is %s%n", t.getName(), t.getState());
        if (t.isAlive()) {
            System.out.println("After start, a thread is said to be alive");
        } else {
            System.out.printf("If main thread is slow, the other thread could terminate in the meantime!");
        }

        // Some other task executed by the main thread
        anotherTask();

        // Now we could reasonably expect the base thread to have reached its end
        assert t.getState() == State.TERMINATED && !t.isAlive();
        System.out.printf("Thread %s state is %s%n", t.getName(), t.getState());
        if (!t.isAlive()) {
            System.out.println("After termination, a thread is no more alive");
        }

        System.out.println("Bye");
    }

    /**
     * Task simulation, just waste some run time
     */
    private static void anotherTask() {
        try {
            // This is just a simulation! The production use of sleep() is very limited!
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
