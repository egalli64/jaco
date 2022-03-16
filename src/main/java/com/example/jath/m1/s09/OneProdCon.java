package com.example.jath.m1.s09;

public class OneProdCon {
    private volatile double result = 0.0;

    private synchronized void producer() {
        result = Math.cbrt(Math.random());
        System.out.printf("%s has produced as result %f%n", Thread.currentThread().getName(), result);
        notifyAll();
    }

    private synchronized void consumer() {
        String tName = Thread.currentThread().getName();
        try {
            while (result == 0.0) {
                System.out.println(tName + " waits for the result");
                wait();
                System.out.println(tName + " wait has ended");
            }

            System.out.printf("%s consumes %f%n", tName, result);
            result = 0;
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) {
        OneProdCon wn = new OneProdCon();

        System.out.println("Starting the producer, that would wait for the consumer");
        Thread consumer = new Thread(wn::consumer, "consumer");
        consumer.start();

        System.out.println("Starting the consumer, that would wait for the producer");
        Thread producer = new Thread(wn::producer, "producer");
        producer.start();

        try {
            consumer.join();
            producer.join();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        System.out.println("Bye");
    }
}
