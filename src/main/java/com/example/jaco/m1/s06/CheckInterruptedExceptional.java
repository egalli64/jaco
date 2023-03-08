/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s06;

/**
 * What happens when an interrupt is sent to a waiting (here, sleeping) thread
 */
public class CheckInterruptedExceptional {
    /**
     * A thread is created and started. After a while an interrupt is sent to it. However, that thread
     * could be waiting for something to happen (here, the wait is simulated by sleeping). So the
     * interrupt could be managed in the "normal" way, or could require the catch of an
     * InterruptedException.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("In thread " + Thread.currentThread().getName());

        // What is going to be executed by the other thread. Only an interrupt will stop it.
        Runnable runnable = () -> {
            final Thread cur = Thread.currentThread();
            System.out.println("In thread " + cur.getName());

            try {
                while (!cur.isInterrupted()) {
                    int i = 1;
                    System.out.print("Simulating that " + cur.getName() + " is waiting on a resource ... ");
                    // This is just a simulation! The use of sleep() in production code is very limited!
                    Thread.sleep(2);
                    while (i % 100 != 0) {
                        System.out.println(i++);
                    }
                }
                // thread interrupted when runnable
                System.out.printf("Someone has interrupted %s!%n", cur.getName());
            } catch (InterruptedException e) {
                // thread interrupted when waiting, sleeping, or otherwise occupied
                System.out.printf("InterruptedException detected in %s!%n", cur.getName());
                // reset the flag on the current thread as interrupted
                cur.interrupt();
            }

            // Thread has been interrupted, terminate it
            System.out.printf("Thread %s is done%n", cur.getName());
        };

        Thread other = new Thread(runnable, "other");

        System.out.println("!!! Two threads are going to compete on System.out !!!");
        System.out.println("!!! Output could be garbled  !!!");

        other.start();

        try {
            System.out.println("Simulating a job in main, let other thread having some fun ...");
            // This is just a simulation! The use of sleep() in production code is very limited!
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        System.out.println("Thread main has enough of other thread!");
        other.interrupt();
        System.out.println("Bye");
    }
}
