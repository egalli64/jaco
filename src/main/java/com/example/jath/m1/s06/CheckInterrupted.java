package com.example.jath.m1.s06;

public class CheckInterrupted {
    public static void main(String[] args) {
        System.out.println("In thread " + Thread.currentThread().getName());

        Thread other = new Thread(() -> {
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
        }, "other");

        System.out.printf("The thread %s has not started yet%n", other.getName());
        System.out.println("!!! Output could be garbled  !!!");
        System.out.println("!!! Two threads are competing on System.out !!!");

        other.start();

        for (int i = 0; i < 100; i++) {
            System.out.print("Simulating a job in main ... ");
            System.out.println(i);
        }

        System.out.println("Thread main has enough of other thread!");
        other.interrupt();

        System.out.printf("Thread %s is done%n", Thread.currentThread().getName());
    }
}
