package com.example.jath.m1.s09;

public class OneProdManyCons {
    private volatile double result = 0.0;

    public static synchronized void checkThreadStates() {
        System.out.println("Checking thread states from " + Thread.currentThread().getName());
        Thread[] ts = new Thread[6];
        // Thread::enumerate() should be used only for debugging and monitoring purposes
        int count = Thread.enumerate(ts);
        for (int i = 0; i < count; i++) {
            System.out.printf("%s is %s%n", ts[i].getName(), ts[i].getState());
        }
    }

    private synchronized void producer() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Producer is about to generate a result");
                result = Math.cbrt(Math.random());
                System.out.println("Producer has produced as result " + result);

                checkThreadStates();
                notifyAll();
                while (result != 0.0) {
                    System.out.println("Producer waits the result to be consumed");
                    wait(500);
                    System.out.println("Producer wait has ended");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Producer has been interrupted");
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Producer has stopped producing values");
        }
    }

    private synchronized void consumer() {
        String tName = Thread.currentThread().getName();
        try {
            while (result == 0.0) {
                System.out.println(tName + " waits for the result");
                wait();
                System.out.println(tName + " wait is ended");
            }

            System.out.printf("Consumer %s consumes %f and then notifies all about it%n", tName, result);
            result = 0.0;

            checkThreadStates();
            notifyAll();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        System.out.println(tName + " exits consumer()");
    }

    public static void main(String[] args) throws InterruptedException {
        OneProdManyCons wn = new OneProdManyCons();

        Thread producer = new Thread(wn::producer, "TP");

        Thread[] consumers = { //
                new Thread(wn::consumer, "TC1"), //
                new Thread(wn::consumer, "TC2"), //
                new Thread(wn::consumer, "TC3") //
        };

        producer.start();
        for (Thread consumer : consumers) {
            consumer.start();
        }

        System.out.println("All threads started, main waits the consumers to join");
        for (Thread consumer : consumers) {
            consumer.join();
        }

        System.out.println("No more consumer, interrupting producer");
        producer.interrupt();
        producer.join();

        System.out.println("Bye");
    }
}
