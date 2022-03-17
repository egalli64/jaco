package com.example.jath.m1.s13;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OneProdCon {
    private volatile double result;
    private Lock lock;
    private Condition available;

    public OneProdCon() {
        this.result = 0.0;
        this.lock = new ReentrantLock();
        this.available = lock.newCondition();
    }

    private void producer() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " enter producer()");

        try {
            lock.lock();
            result = Math.cbrt(Math.random());
            System.out.printf("%s has produced as result %f%n", name, result);
            available.signal();
        } finally {
            lock.unlock();
        }
    }

    private void consumer() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " enter consumer()");

        try {
            lock.lock();
            while (result == 0.0) {
                System.out.println(name + " waits for the result");
                available.await();
                System.out.println(name + " wait is ended");
            }

            System.out.printf("%s consumes %f%n", name, result);
            result = 0.0;
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        OneProdCon wn = new OneProdCon();

        Thread ts[] = { new Thread(wn::consumer, "TC"), new Thread(wn::producer, "TP") };

        for (Thread t : ts) {
            t.start();
        }

        for (Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

        System.out.println("Bye");
    }
}
