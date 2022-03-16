package com.example.jath.m1.s07;

public class Main {
    public static void main(String[] args) {
        Thread t0 = new Thread(() -> System.out.println("A message from " + Thread.currentThread().getName()));

        if (!t0.isAlive()) {
            System.out.printf("Before starting it, %s is not yet alive%n", t0.getName());
        } else {
            System.out.println("You should not get this message");
        }
        t0.start();
        if (t0.isAlive()) {
            System.out.printf("After starting it, %s is alive%n", t0.getName());
        } else {
            System.out.println("You should not get this message");
        }

        try {
            t0.join();
            System.out.println(t0.getName() + " joined");
        } catch (InterruptedException e) {
            System.out.printf("Main thread interrupted while waiting %s to join%n" + t0.getName());
            // we usually handle the interruption, at least resetting the interrupt flag
            Thread.currentThread().interrupt();
        }

        if (!t0.isAlive()) {
            System.out.println("The other thread is not alive anymore");
        } else {
            System.out.println("You should not get this message, join interrupted?");
        }
    }
}
