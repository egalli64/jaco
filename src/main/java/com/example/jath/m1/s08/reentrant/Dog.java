package com.example.jath.m1.s08.reentrant;

public class Dog {
    public synchronized void bark() {
        System.out.println("Barking as a dog");
    }
}
