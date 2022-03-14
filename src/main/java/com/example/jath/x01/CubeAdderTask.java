package com.example.jath.x01;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

@SuppressWarnings("serial")
public class CubeAdderTask extends RecursiveTask<Double> {
    private static final int THRESHOLD = 200_000;

    private double[] data;
    private int begin;
    private int end;

    public CubeAdderTask(double[] data, int begin, int end) {
        this.data = data;
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Double compute() {
        if (end - begin <= THRESHOLD) {
            double result = 0.0;
            for (int i = begin; i < end; i++) {
                result += Math.pow(data[i], 3);
            }
            return result;
        } else {
            CubeAdderTask left = new CubeAdderTask(data, begin, (begin + end) / 2);
            CubeAdderTask right = new CubeAdderTask(data, (begin + end) / 2, end);

            ForkJoinTask.invokeAll(left, right);
            // or, take care directly of details
            // left.fork();
            // right.compute();
            // left.join();

            try {
                return left.get() + right.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
