package com.example.jath.x01;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.DoubleStream;

public class CubeAdder {
    public static double plain(double[] data) {
        double result = 0;
        for (int i = 0; i < data.length; i++) {
            result += Math.pow(data[i], 3);
        }
        return result;
    }

    public static double classic(double[] data) {
        CubeAdderRunner left = new CubeAdderRunner(data, 0, data.length / 2);
        CubeAdderRunner right = new CubeAdderRunner(data, data.length / 2, data.length);

        Thread[] ts = { new Thread(left), new Thread(right) };

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

        return left.result() + right.result();
    }

    public static double recursiveAction(double[] data) {
        CubeAdderAction action = new CubeAdderAction(data, 0, data.length);
        new ForkJoinPool().invoke(action);
        return action.result();
    }

    public static double recursiveTask(double[] data) throws Exception {
        CubeAdderTask task = new CubeAdderTask(data, 0, data.length);
        new ForkJoinPool().invoke(task);
        return task.get();
    }

    public static void main(String[] args) {
        double[] data = DoubleStream.generate(Math::random).limit(16_000_000).toArray();

        System.out.println("Plain adder");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            double result = plain(data);
            System.out.printf("Sum %f computed in ~ %d ms%n", result, (System.currentTimeMillis() - start));
        }

        System.out.println("Classic MultiThreading on two parts");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            double result = classic(data);
            System.out.printf("Sum %f computed in ~ %d ms%n", result, (System.currentTimeMillis() - start));
        }

        System.out.println("Fork Join Recursive Action");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            double result = recursiveAction(data);
            System.out.printf("Sum %f computed in ~ %d ms%n", result, (System.currentTimeMillis() - start));
        }

        System.out.println("Fork Join Recursive Task");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            try {
                double result = recursiveTask(data);
                System.out.printf("Sum %f computed in ~ %d ms%n", result, (System.currentTimeMillis() - start));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
