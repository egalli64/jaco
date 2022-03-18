package com.example.jath.m1.s08.reentrant;

public class Poodle extends Dog {
    @Override
    public synchronized void bark() {
        super.bark();
        System.out.println("Bark variation as a poodle");
    }
}
