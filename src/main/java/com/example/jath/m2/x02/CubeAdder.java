package com.example.jath.m2.x02;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.DoubleStream;

public class CubeAdder {
    public static void main(String[] args) {
        double[] data = DoubleStream.generate(Math::random).limit(16_000_000).toArray();

        System.out.println("Fork Join Recursive Task");
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            try {
                CubeAdderTask task = new CubeAdderTask(data, 0, data.length);
                new ForkJoinPool().invoke(task);

                System.out.printf("Sum %f computed in ~ %d ms%n", task.get(), (System.currentTimeMillis() - start));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
