package com.example.jath.m1.s08;

public class SynchoOnObject {
    private Object lockF = new Object();
    private Object lockG = new Object();

    public static void main(String[] args) {
        SynchoOnObject som = new SynchoOnObject();

        Thread[] threads = { //
                new Thread(som::syncOnThis, "This1"), //
                new Thread(som::syncOnF, "F1"), //
                new Thread(som::syncOnG, "G1"), //
                new Thread(som::syncOnThis, "This2"), //
                new Thread(som::syncOnF, "F2"), //
                new Thread(som::syncOnG, "G2") //
        };

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new IllegalArgumentException(e);
            }
        }
        System.out.println("Bye from " + Thread.currentThread().getName());
    }

    private double aJob() {
        return Math.cbrt(Math.random());
    }

    public synchronized void syncOnThis() {
        System.out.println(Thread.currentThread().getName() + " enter syncOnThis()");
        System.out.println(Thread.currentThread().getName() + " " + aJob());
        System.out.println(Thread.currentThread().getName() + " exit syncOnThis()");
    }

    public void syncOnF() {
        synchronized (lockF) {
            System.out.println(Thread.currentThread().getName() + " enter sync block on F");
            System.out.println(Thread.currentThread().getName() + " " + aJob());
            System.out.println(Thread.currentThread().getName() + " exit sync block on F");
        }
    }

    public void syncOnG() {
        synchronized (lockG) {
            System.out.println(Thread.currentThread().getName() + " enter sync block on G");
            System.out.println(Thread.currentThread().getName() + " " + aJob());
            System.out.println(Thread.currentThread().getName() + " exit sync block on G");
        }
    }
}
