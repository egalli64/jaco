package com.example.jath.m2.s06;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {
    private static final int NUMBER_OF_THREADS = 3;
    private static int counter = 0;

    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(NUMBER_OF_THREADS, () -> {
            System.out.println(Thread.currentThread().getName() + " go!");
        });

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Runnable worker = () -> {
            String name = Thread.currentThread().getName();

            try {
                System.out.printf("%s: %g%n", name, aJob((int) (Math.random() * 1_000)));
                cb.await();
            } catch (BrokenBarrierException e) {
                System.out.println(name + " barrier has been broken");
                return;
            } catch (InterruptedException e) {
                System.out.println(name + " wait has been interrupted");
                return;
            }

            try {
                lock.lock();
                System.out.println(name + " working [" + counter + "]");
                if (++counter == NUMBER_OF_THREADS) {
                    counter = 0;
                    condition.signal();
                }
            } finally {
                lock.unlock();
            }
        };

        Thread[] ts = { //
                new Thread(worker), //
                new Thread(worker), //
                new Thread(worker), //
                new Thread(worker), //
                new Thread(worker) //
        };

        for (Thread t : ts) {
            t.start();
        }

        try {
            lock.lock();
            condition.await();
            for (Thread t : ts) {
                t.interrupt();
                break;
            }
//            cb.reset();
        } catch (InterruptedException e) {
            System.out.println("No exception expected here");
            throw new IllegalStateException(e);
        } finally {
            lock.unlock();
        }
    }

    private static double aJob(int counter) {
        double result = 0.0;
        for (int i = 0; i < counter; i++) {
            result += Math.cbrt(i);
        }
        return result;
    }
}
