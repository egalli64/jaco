/*
 * Introduction to Java Thread
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m1.s03;

import java.lang.Thread.State;

/**
 * Show waiting and time waiting threads
 * 
 * Enable assertion with -ea VM argument
 */
public class Waiting {
    /**
     * Create a thread that goes immediately in timed waiting state. Put the main thread in wait for the
     * end of the child thread, then terminate.
     * 
     * @param args not used
     * @throws InterruptedException if wait is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        // Create and start the thread "waited", that runs the specified method
        Thread t1 = new Thread(Waiting::aMethod, "waited");
        t1.start();

        // Do something else on main thread, so we can safely assume t1 kicks in
        Jobs.takeTime(50);

        // Now t1 is expected to be in a timed waiting
        assert t1.getState() == State.TIMED_WAITING;
        System.out.printf("Checking %s thread from main, its state is %s%n", t1.getName(), t1.getState());

        // Put the main thread in waiting for t1 to terminate
        t1.join();

        System.out.println("Bye");
    }

    /**
     * A method meant to run in its own thread.
     * 
     * It simulates a timed waiting on a resource, then check that main is waiting for its termination.
     */
    public static void aMethod() {
        System.out.printf("Simulating a wait on a resource for %s thread%n", Thread.currentThread().getName());
        try {
            // This is just a simulation! The use of sleep() in production code is very limited!
            Thread.sleep(500);

            System.out.printf("Checking from '%s' thread: ", Thread.currentThread().getName());
            Thread[] ts = new Thread[2];
            // Thread::enumerate() should be used for debugging and monitoring purposes only
            int count = Thread.enumerate(ts);
            for (int i = 0; i < count; i++) {
                if (ts[i].getName().equals("main")) {
                    State state = ts[i].getState();

                    // the main thread state should be waiting - being in join on this thread
                    assert state == State.WAITING;
                    System.out.println("main state is " + state);
                    break;
                }
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
