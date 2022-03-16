package com.example.jath.m1.s08;

public class SynchroStatic {
    private static double aJob() {
        return Math.cbrt(Math.random());
    }

    private synchronized static void m1() {
        System.out.println(Thread.currentThread().getName() + " enter static m1()");
        System.out.println(Thread.currentThread().getName() + " static m1() " + aJob());
        System.out.println(Thread.currentThread().getName() + " exit static m1()");
    }

    private synchronized static void m2() {
        System.out.println(Thread.currentThread().getName() + " enter static m2()");
        System.out.println(Thread.currentThread().getName() + " static m2() " + aJob());
        System.out.println(Thread.currentThread().getName() + " exit static m2()");
    }

    public synchronized void hello1() {
        System.out.println(Thread.currentThread().getName() + " enter hello1()");
        m1();
        System.out.println(Thread.currentThread().getName() + " hello1() " + aJob());
        m2();
        System.out.println(Thread.currentThread().getName() + " exit hello1()");
    }

    public synchronized void hello2() {
        System.out.println(Thread.currentThread().getName() + " enter hello2()");
        m1();
        System.out.println(Thread.currentThread().getName() + " hello2() " + aJob());
        m2();
        System.out.println(Thread.currentThread().getName() + " exit hello2()");
    }

    public static void main(String[] args) {
        SynchroStatic sy1 = new SynchroStatic();
        SynchroStatic sy2 = new SynchroStatic();

        Thread[] threads = { //
                new Thread(sy1::hello1, "ObjectOneA"), //
                new Thread(sy1::hello2, "ObjectOneB"), //
                new Thread(sy2::hello1, "ObjectTwoA"), //
                new Thread(sy2::hello2, "ObjectTwoB") //
        };

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        System.out.println("Bye from " + Thread.currentThread().getName());
    }
}
