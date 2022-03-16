package com.example.jath.m1.s05;

public class MainForRunnable {
    public static void main(String[] args) {
        System.out.println("In thread " + Thread.currentThread().getName());

        Runnable my = new MyRunnable();
        Thread other = new Thread(my, "other");
        System.out.printf("The thread %s has not started yet%n", other.getName());

        other.start();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            System.out.printf("%s sleep interrupted%n", Thread.currentThread().getName());
            throw new IllegalStateException(e);
        }

        System.out.printf("Thread %s is done%n", Thread.currentThread().getName());
    }
}
