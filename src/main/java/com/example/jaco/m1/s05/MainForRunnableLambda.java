/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s05;

/**
 * Working on a thread by composition
 * 
 * Injecting a runnable defined locally
 */
public class MainForRunnableLambda {
    /**
     * Create and start a thread by instancing a Thread injected with a Runnable
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("In thread " + Thread.currentThread().getName());

        // The behavior we want to get from the other thread
        Runnable runnable = () -> {
            System.out.println("In thread " + Thread.currentThread().getName());

            try {
                // This is just a simulation! The use of sleep() in production code is very limited!
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            System.out.printf("Thread %s is done%n", Thread.currentThread().getName());
        };

        // Inject the runnable in a thread object
        Thread other = new Thread(runnable, "other");
        System.out.printf("%s state is %s%n", other.getName(), other.getState());

        // Start the thread
        other.start();
        System.out.printf("%s state is %s%n", other.getName(), other.getState());

        // Doing something else in the main thread
        try {
            // This is just a simulation! The use of sleep() in production code is very limited!
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        // Notice that there is no guarantee that the other thread has already terminated!
        System.out.printf("About to end main, %s state is %s%n", other.getName(), other.getState());
    }
}
