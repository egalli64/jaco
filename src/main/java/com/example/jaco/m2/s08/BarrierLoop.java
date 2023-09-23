/*
 * Introduction to Java Concurrency
 * 
 * https://github.com/egalli64/jaco
 */
package com.example.jaco.m2.s08;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Using a CyclicBarrier in a loop, plus interrupting workers and reset barrier.
 */
public class BarrierLoop extends ProblemFrame {
    private static final Logger log = LoggerFactory.getLogger(BarrierLoop.class);
    private static final int LOOP_NR = 3;

    /**
     * Create a CyclicBarrier for the workers and the main thread.
     * <p>
     * In a loop, create a thread for a batch of workers and start them. Each worker
     * does its job and waits on the barrier. The main thread wait for the barrier
     * to break, then check for the result generated by the workers.
     * <p>
     * After that, run a couple of worker but then reset the barrier.
     * <p>
     * Finally, run a couple of worker but then interrupt one of them.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Enter");

        CyclicBarrier barrier = new CyclicBarrier(TASK_NR + 1);
        DoubleAdder accumulator = new DoubleAdder();

        Runnable worker = () -> {
            log.trace("Enter");

            double value = job(100);
            log.debug("Value {}", value);
            accumulator.add(value);
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException ex) {
                String name = Thread.currentThread().getName();
                long time = System.nanoTime() / 1000 % 10_000;
                System.out.printf("%s, wait interrupted @%d %s %n", name, time, ex);
            }

            log.trace("Exit");
        };

        // Loop on the barrier
        String name = Thread.currentThread().getName();
        for (int i = 0; i < LOOP_NR; i++) {
            Stream.generate(() -> new Thread(worker)).limit(TASK_NR).forEach(Thread::start);

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException ex) {
                log.warn("Wait on barrier interrupted", ex);
            }

            System.out.printf("In %s: %f (%d)%n", name, accumulator.sum(), i);
        }

        // Prepare another loop, but then reset it
        new Thread(worker, "B1").start();
        new Thread(worker, "B2").start();

        // BrokenBarrierException expected for both B1 and B2
        System.out.println("Giving time for the workers to kick in: " + job(10));
        barrier.reset();

        // Prepare another loop, but then interrupt a worker
        Thread ti1 = new Thread(worker, "I1");
        ti1.start();
        new Thread(worker, "I2").start();

        // InterruptedException expected for I1, BrokenBarrierException for I2
        ti1.interrupt();
        log.trace("Exit");
    }
}
